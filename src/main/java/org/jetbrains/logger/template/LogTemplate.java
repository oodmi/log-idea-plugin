package org.jetbrains.logger.template;

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class LogTemplate extends PostfixTemplate {

    private final boolean isApplicable;

    protected LogTemplate(@NotNull String name, @NotNull String example) {
        super(name, example);
        isApplicable = true;
    }

    @Override
    public boolean isApplicable(@NotNull PsiElement psiElement, @NotNull Document document, int i) {
        System.out.println("LogTemplate.isApplicable");
        return isApplicable;
    }

    @Override
    public void expand(@NotNull PsiElement psiElement, @NotNull Editor editor) {
        System.out.println("LogTemplate.expand");
    }
}
