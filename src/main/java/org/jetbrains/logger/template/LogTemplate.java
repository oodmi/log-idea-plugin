package org.jetbrains.logger.template;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.impl.TextExpression;
import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.IS_NON_VOID;
import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.selectorTopmost;

public class LogTemplate extends StringBasedPostfixTemplate {

    private String templateString;

    protected LogTemplate(@NotNull String name, @NotNull String example, @NotNull String templateString) {
        super(name, example, selectorTopmost(IS_NON_VOID));
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
        template.addVariable("logger", index, index, true);
    }
}
