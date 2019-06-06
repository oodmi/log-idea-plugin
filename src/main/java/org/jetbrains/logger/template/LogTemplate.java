package org.jetbrains.logger.template;

import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.IS_NON_VOID;
import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.selectorAllExpressionsWithCurrentOffset;
import static org.jetbrains.logger.utils.LogUtils.CURSOR;
import static org.jetbrains.logger.utils.LogUtils.LOGGER;
import static org.jetbrains.logger.utils.LogUtils.TYPE;
import static org.jetbrains.logger.utils.LogUtils.VAR;
import static org.jetbrains.logger.utils.LogUtils.getLoggerName;
import static org.jetbrains.logger.utils.LogUtils.getParent;
import static org.jetbrains.logger.utils.LogUtils.isTypeString;
import static org.jetbrains.logger.utils.LogUtils.replaceLast;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.TextExpression;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplatesUtils;
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAssignmentExpression;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.logger.LogTemplateProvider;

import java.util.Objects;

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
        if (parent instanceof PsiExpressionStatement) {
            return getTemplateInsteadOfParent(element);
        }

        if (Objects.nonNull(PsiTreeUtil.getParentOfType(element, PsiDeclarationStatement.class))
                && parent instanceof PsiLocalVariable) {
            String localVariable = ((PsiLocalVariable) parent).getName();
            return getTemplateAfterParent(element, localVariable);
        }

        if (parent instanceof PsiAssignmentExpression) {
            final String localVariable = ((PsiAssignmentExpression) parent).getLExpression().getText();
            return getTemplateAfterParent(element, localVariable);
        }

        if (element instanceof PsiReferenceExpression) {
            return getTemplateBeforeParent(element);
        }

        return getTemplateWithVar(element);
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
        System.out.println("\nparent = " + parent);
        System.out.println("expr = " + expr + "\n");
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

        if (parent instanceof PsiExpressionStatement) {
            setVariableInsteadOfParent(template, element, log);
            return;
        }

        if (element instanceof PsiReferenceExpression) {
            setVariableBeforeParent(template, element, log);
            return;
        }

        if (Objects.nonNull(PsiTreeUtil.getParentOfType(element, PsiDeclarationStatement.class))
                && parent instanceof PsiLocalVariable) {
            setVariableAfterParent(template, element, log);
            return;
        }

        if (parent instanceof PsiAssignmentExpression) {
            setVariableAfterParent(template, element, log);
            return;
        }

        setVariableWithVar(template, element, log);

    }

    private void setVariableInsteadOfParent(@NotNull Template template, @NotNull PsiElement element, TextExpression log) {
        /*
            this block just use expression. i.e:
            "something".logi
         -> log.info("something");
        */
        if ((element instanceof PsiMethodCallExpression)
                || (element instanceof PsiReferenceExpression)
                || !isTypeString(element)) {
            template.addVariable(EXPR, new TextExpression(element.getText()), false);
        }
        template.addVariable(LOGGER, log, log, false);
        if (!(element instanceof PsiReferenceExpression)) {
            template.addVariable(CURSOR, new TextExpression(""), true);
        }
    }

    private String getTemplateInsteadOfParent(@NotNull PsiElement element) {
        if (!(element instanceof PsiMethodCallExpression)
                && !(element instanceof PsiReferenceExpression)
                && isTypeString(element)) {
            String text = "\"" + "$" + CURSOR + "$" + element.getText().substring(1);
            String template = "$" + LOGGER + "$." + level + "("
                    + text
                    + ");$END$";
            System.out.println("\nLogTemplate.getTemplateInsteadOfParent\n");
            return template;
        }
        final String temp = element instanceof PsiReferenceExpression ? "$" + EXPR + "$=" : "$" + CURSOR + "$";

        String template = "$" + LOGGER + "$." + level + "("
                + "\"" + temp + "\" + "
                + "$" + EXPR + "$);$END$";
        System.out.println("\nLogTemplate.getTemplateInsteadOfParent\n");
        return template;
    }

    private void setVariableAfterParent(@NotNull Template template, @NotNull PsiElement element, TextExpression log) {
        /*
         use local variable. i.e:
           int i = 1.logi;
        -> int i = 1;
           log.info("" + i);
        */
        template.addVariable(LOGGER, log, log, false);
    }

    private String getTemplateAfterParent(@NotNull PsiElement element, String localVariable) {
        final String template = getParent(element).getText() + "\n"
                + "$" + LOGGER + "$." + level + "("
                + "\"" + localVariable + "=\" + "
                + localVariable + ");$END$";
        System.out.println("\nLogTemplate.getTemplateAfterParent\n");
        return template;
    }

    private void setVariableBeforeParent(@NotNull Template template, @NotNull PsiElement element, TextExpression log) {
        /*
            use local variable. i.e:
            int i = 1; method1(method2(i.logi));
        ->  log.info("" + i);
            method1(method2(i));
        */
        template.addVariable(LOGGER, log, log, false);
        template.addVariable(EXPR, new TextExpression(element.getText()), false);
    }

    private String getTemplateBeforeParent(@NotNull PsiElement element) {
        String template = "$" + LOGGER + "$." + level + "("
                + "\"" + "$" + EXPR + "$" + "=\" + "
                + "$" + EXPR + "$);\n"
                + getParent(element).getText()
                + "$END$";
        System.out.println("\nLogTemplate.getTemplateBeforeParent\n");
        return template;
    }

    private void setVariableWithVar(@NotNull Template template, @NotNull PsiElement element, TextExpression log) {
        /*
            this block creates variable. i.e.:
            int i = 1;
            method1(method2(i).logi);
         -> int i = 1;
            var s = method2(i);
            log.info("" + s);
            method1(s)'
         */
        PsiExpression psiExpression = (PsiExpression) element;
        final PsiType type = psiExpression.getType();
        if (Objects.nonNull(type)) {
            System.out.println("type.getCanonicalText() = " + type.getCanonicalText());
            final TextExpression typeName = new TextExpression(type.getCanonicalText());
            template.addVariable(TYPE, typeName, typeName, true);
        }

        final TextExpression varName = new TextExpression("newVar");
        template.addVariable(VAR, varName, varName, true);

        template.addVariable(LOGGER, log, log, false);

        template.addVariable(EXPR, new TextExpression(element.getText()), false);
    }

    private String getTemplateWithVar(@NotNull PsiElement element) {
        final String endText = replaceLast(getParent(element).getText(),
                                           element.getText(),
                                           "$" + VAR + "$");
        final String template = "$" + TYPE + "$" + " $" + VAR + "$ = " + "$" + EXPR + "$;\n"
                + "$" + LOGGER + "$." + level + "("
                + "\"" + "$" + VAR + "$" + "=\" + "
                + "$" + VAR + "$);\n"
                + endText + "$END$";
        System.out.println("LogTemplate.getTemplateWithVar");
        return template;
    }
}
