package org.jetbrains.logger.template;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.TextExpression;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplatesUtils;
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAssignmentExpression;
import com.intellij.psi.PsiBinaryExpression;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.logger.LogTemplateProvider;

import java.util.Objects;

import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.IS_NON_VOID;
import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.selectorAllExpressionsWithCurrentOffset;
import static org.jetbrains.logger.utils.LogUtils.LOGGER;
import static org.jetbrains.logger.utils.LogUtils.TYPE;
import static org.jetbrains.logger.utils.LogUtils.VAR;
import static org.jetbrains.logger.utils.LogUtils.getLoggerName;
import static org.jetbrains.logger.utils.LogUtils.getParent;
import static org.jetbrains.logger.utils.LogUtils.isNeedBraces;
import static org.jetbrains.logger.utils.LogUtils.replaceLast;

public class LogTemplate extends StringBasedPostfixTemplate {

    private String level;

    public LogTemplate(@NotNull String name, @NotNull String example, @NotNull String level, LogTemplateProvider logTemplateProvider) {
        super(name, example, selectorAllExpressionsWithCurrentOffset(IS_NON_VOID), logTemplateProvider);
        this.level = level;
    }

    @Nullable
    @Override
    public String getTemplateString(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        boolean needBraces = isNeedBraces(element);
        if (parent instanceof PsiExpressionStatement) {
            return "$" + LOGGER + "$." + level + "("
                    + (needBraces ? "\"\" + " : "")
                    + "$" + EXPR + "$);$END$";
        }

        PsiDeclarationStatement parentOfType = PsiTreeUtil.getParentOfType(element, PsiDeclarationStatement.class);
        if (Objects.nonNull(parentOfType)) {
            if (parent instanceof PsiLocalVariable) {
                String localVariable = ((PsiLocalVariable) parent).getName();
                final String template = getParent(element).getText() + "\n"
                        + "$" + LOGGER + "$." + level + "("
                        + (needBraces ? "\"\" + " : "")
                        + localVariable + ");$END$";
                return template;
            }
        }

        if (parent instanceof PsiAssignmentExpression) {
            final String localVariable = ((PsiAssignmentExpression) parent).getLExpression().getText();
            final String template = getParent(element).getText() + "\n"
                    + "$" + LOGGER + "$." + level + "("
                    + (needBraces ? "\"\" + " : "")
                    + localVariable + ");$END$";
            return template;
        }

        if (element instanceof PsiReferenceExpression) {
            return "$" + LOGGER + "$." + level + "("
                    + (needBraces ? "\"\" + " : "")
                    + "$" + EXPR + "$);\n"
                    + getParent(element).getText()
                    + "$END$";
        }

        final String endText = replaceLast(getParent(element).getText(),
                                           element.getText(),
                                           "$" + VAR + "$");
        final String template = "$" + TYPE + "$" + " $" + VAR + "$ = " + "$" + EXPR + "$;\n"
                + "$" + LOGGER + "$." + level + "("
                + (needBraces ? "\"\" + " : "")
                + "$" + VAR + "$);\n"
                + endText + "$END$";
        return template;
    }

    @Override
    public void expandForChooseExpression(@NotNull PsiElement expr, @NotNull Editor editor) {
        //check available logger
        String loggerName = getLoggerName(expr);
        if (loggerName == null) {
            PostfixTemplatesUtils.showErrorHint(expr.getProject(), editor);
            return;
        }

        Project project = expr.getProject();
        Document document = editor.getDocument();

        //delete ot not current expression
        PsiElement parent = expr.getParent();
        if (parent instanceof PsiExpressionStatement) {
            if (parent.getText().endsWith(";")) {
                document.deleteString(parent.getTextRange().getStartOffset(), parent.getTextRange().getEndOffset());
            } else {
                document.deleteString(expr.getTextRange().getStartOffset(), expr.getTextRange().getEndOffset());
            }
        } else {
            PsiElement elementForRemoving = getParent(expr);
            document.deleteString(elementForRemoving.getTextRange().getStartOffset(), elementForRemoving.getTextRange().getEndOffset());
        }

        TemplateManager manager = TemplateManager.getInstance(project);

        Template template = super.createTemplate(manager, getTemplateString(expr));
        setVariables(template, expr);
        manager.startTemplate(editor, template);
    }

    @Override
    public void setVariables(@NotNull Template template, @NotNull PsiElement element) {
        String loggerName = getLoggerName(element);
        TextExpression log = new TextExpression(loggerName);
        PsiElement parent = element.getParent();
        if (!(parent instanceof PsiExpressionStatement)) {

            /*
             use local variable. i.e:
                int i = 1; method1(method2(i.logi));
            ->  log.info("" + i);
                method1(method2(i));
            */
            if (element instanceof PsiReferenceExpression) {
                template.addVariable(LOGGER, log, log, true);
                template.addVariable(EXPR, new TextExpression(element.getText()), false);
                return;
            }

            /*
             use local variable. i.e:
               int i = 1.logi;
            -> int i = 1;
               log.info("" + i);
            */
            PsiDeclarationStatement parentOfType = PsiTreeUtil.getParentOfType(element, PsiDeclarationStatement.class);
            if (parent instanceof PsiAssignmentExpression || Objects.nonNull(parentOfType)) {
                // if element is PsiBinaryExpression or PsiNewExpression, we'll need to use creation of variable
                //if parent is PsiExpressionList, we'll need to use creation of variable
                // otherwise just use local variable
                if (!(element instanceof PsiBinaryExpression)
                        && !(element instanceof PsiNewExpression)
                        && !(parent instanceof PsiExpressionList)) {
                    //                if (!(element instanceof PsiMethodCallExpression)) {
                    template.addVariable(LOGGER, log, log, true);
                    return;
//                }
                }
            }

            /*
                this block creates variable. i.e.:
                int i = 1;
                method1(method2(i).logi);
             -> int i = 1;
                var s = method2(i);
                log.info("" + s);
                method1(s)'
             */
            {
                PsiExpression psiExpression = (PsiExpression) element;
                final PsiType type = psiExpression.getType();
                if (Objects.nonNull(type)) {
                    System.out.println("type.getCanonicalText() = " + type.getCanonicalText());
                    final TextExpression typeName = new TextExpression(type.getCanonicalText());
                    template.addVariable(TYPE, typeName, typeName, true);
                }

                final TextExpression varName = new TextExpression("newVar");
                template.addVariable(VAR, varName, varName, true);

                template.addVariable(LOGGER, log, log, true);

                template.addVariable(EXPR, new TextExpression(element.getText()), false);
            }
        } else {
            /*
                this block just use expression. i.e:
                "something".logi
             -> log.info("something");
             */
            template.addVariable(EXPR, new TextExpression(element.getText()), false);
            template.addVariable(LOGGER, log, log, true);
        }
    }
}
