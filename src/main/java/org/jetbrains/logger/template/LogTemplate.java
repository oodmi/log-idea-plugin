package org.jetbrains.logger.template;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.TextExpression;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplatesUtils;
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.logger.LogTemplateProvider;

import java.util.Arrays;
import java.util.Objects;

import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.IS_NON_VOID;
import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.selectorAllExpressionsWithCurrentOffset;
import static org.jetbrains.logger.utils.LogUtils.*;

public class LogTemplate extends StringBasedPostfixTemplate {

    private String level;

    public LogTemplate(@NotNull String name, @NotNull String example, @NotNull String level, LogTemplateProvider logTemplateProvider) {
        super(name, example, selectorAllExpressionsWithCurrentOffset(IS_NON_VOID), logTemplateProvider);
        this.level = level;
    }

    private static String getLoggerName(@NotNull PsiElement element) {
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        if (Objects.isNull(psiClass)) {
            return null;
        }
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            String qualifiedName = annotation.getQualifiedName();
            System.out.println(qualifiedName);
            if (qualifiedName != null && getLombok().contains(qualifiedName)) {
                return getLombokName();
            }
        }
        PsiField[] allFields = psiClass.getAllFields();
        for (PsiField field : allFields) {
            boolean flag = Arrays.stream(field.getModifiers()).anyMatch(it -> {
                System.out.println(it.name());
                return getModifier().equalsIgnoreCase(it.name());
            });
            if (flag) {
                String clazz = field.getType().getCanonicalText();
                boolean suitable = getLoggers().stream().anyMatch(it -> clazz.toLowerCase().contains(it));
                if (suitable) {
                    return field.getName();
                }
            }
        }
        return null;
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
            PsiLocalVariable localVariable = (PsiLocalVariable) element.getParent();
            final String template = element.getContext().getText() + "\n"
                    + "$" + LOGGER + "$." + level + "("
                    + (needBraces ? "\"\" + " : "")
                    + localVariable.getName() + ");$END$";
            return template;
        }

        if (element instanceof PsiReferenceExpression) {
            return "$" + LOGGER + "$." + level + "("
                    + (needBraces ? "\"\" + " : "")
                    + "$" + EXPR + "$);\n"
                    + getParent(element).getText()
                    + "$END$";
        }

        final String endText = replaceLast(element.getContext().getText(),
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
            document.deleteString(expr.getTextRange().getStartOffset(), expr.getTextRange().getEndOffset());
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

            PsiDeclarationStatement parentOfType = PsiTreeUtil.getParentOfType(element, PsiDeclarationStatement.class);
            if (Objects.nonNull(parentOfType)) {
                template.addVariable(LOGGER, log, log, true);
                return;
            }
            // use local variable. i.e:
            // int i = 1; method1(method2(i.logi));
            // -> log.info("" + i); method1(method2(i));
            if (element instanceof PsiReferenceExpression) {
                template.addVariable(LOGGER, log, log, true);
                template.addVariable(EXPR, new TextExpression(element.getText()), false);
                return;
            }

            PsiExpression psiExpression = (PsiExpression) element;
            final PsiType type = psiExpression.getType();
            if (Objects.nonNull(type)) {
                final TextExpression typeName = new TextExpression(type.getCanonicalText());
                template.addVariable(TYPE, typeName, typeName, true);
            }

            final TextExpression varName = new TextExpression("newVar");
            template.addVariable(VAR, varName, varName, true);

            template.addVariable(LOGGER, log, log, true);

            template.addVariable(EXPR, new TextExpression(element.getText()), false);
        } else {
            template.addVariable(EXPR, new TextExpression(element.getText()), false);
            template.addVariable(LOGGER, log, log, true);
        }
    }

}
