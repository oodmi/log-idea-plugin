package org.jetbrains.logger.template;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.TextExpression;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplatesUtils;
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
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

    private String templateString;

    LogTemplate(@NotNull String name, @NotNull String example, @NotNull String templateString, LogTemplateProvider logTemplateProvider) {
        super(name, example, selectorAllExpressionsWithCurrentOffset(IS_NON_VOID), logTemplateProvider);
        this.templateString = templateString;
    }

    @Nullable
    @Override
    public String getTemplateString(@NotNull PsiElement element) {
        return templateString;
    }

    @Override
    public void expandForChooseExpression(@NotNull PsiElement expr, @NotNull Editor editor) {
        Project project = expr.getProject();
        Document document = editor.getDocument();
        PsiElement elementForRemoving = getElementToRemove(expr);
        document.deleteString(elementForRemoving.getTextRange().getStartOffset(), elementForRemoving.getTextRange().getEndOffset());
        TemplateManager manager = TemplateManager.getInstance(project);

        String templateString = getTemplateString(expr);
        if (templateString == null) {
            PostfixTemplatesUtils.showErrorHint(expr.getProject(), editor);
            return;
        }

        Template template = super.createTemplate(manager, templateString);
        template.addVariable(EXPR, new TextExpression(expr.getText()), false);
        setVariables(template, expr);
        manager.startTemplate(editor, template);
    }

    @Override
    public void setVariables(@NotNull Template template, @NotNull PsiElement element) {
        TextExpression index = new TextExpression("LOG");
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        if (Objects.isNull(psiClass)) {
            return;
        }
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            String qualifiedName = annotation.getQualifiedName();
            System.out.println(qualifiedName);
            if (qualifiedName != null && getLombok().contains(qualifiedName)) {
                TextExpression log = new TextExpression(getLombokName());
                template.addVariable("logger", log, log, true);
                return;
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
                    TextExpression log = new TextExpression(field.getName());
                    template.addVariable("logger", log, log, true);
                    return;
                }
            }
        }

        template.addVariable("logger", index, index, true);
    }

    private String getLombokName() {
        //TODO: calculate name from lombok.config
        return "log";
    }
}
