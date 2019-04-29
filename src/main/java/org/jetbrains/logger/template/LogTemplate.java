package org.jetbrains.logger.template;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.impl.TextExpression;
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.IS_NON_VOID;
import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.selectorAllExpressionsWithCurrentOffset;

public class LogTemplate extends StringBasedPostfixTemplate {

    private String templateString;

    protected LogTemplate(@NotNull String name, @NotNull String example, @NotNull String templateString) {
        super(name, example, selectorAllExpressionsWithCurrentOffset(IS_NON_VOID));
        this.templateString = templateString;
    }

    @Nullable
    @Override
    public String getTemplateString(@NotNull PsiElement element) {
        return templateString;
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
            if (qualifiedName != null && qualifiedName.contains("slf4j")) {
                TextExpression log = new TextExpression("log");
                template.addVariable("logger", log, log, true);
                return;
            }
        }
        PsiField[] allFields = psiClass.getAllFields();
        for (PsiField allField : allFields) {
            System.out.println(allField);
        }

        template.addVariable("logger", index, index, true);
    }
}
