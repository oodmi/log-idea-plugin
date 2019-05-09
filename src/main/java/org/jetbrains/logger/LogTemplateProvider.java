package org.jetbrains.logger;

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.logger.template.impl.*;

import java.util.Set;

public class LogTemplateProvider implements PostfixTemplateProvider {

    private final Set<PostfixTemplate> templates = ContainerUtil.newHashSet(
            new PostfixTemplate[]{
                    new LogInfoTemplate(this),
                    new LogDebugTemplate(this),
                    new LogErrorTemplate(this),
                    new LogTraceTemplate(this),
                    new LogWarnTemplate(this)
            });

    public LogTemplateProvider() {
        System.out.println("LogTemplateProvider.LogTemplateProvider");
    }

    @NotNull
    @Override
    public Set<PostfixTemplate> getTemplates() {
        System.out.println("LogTemplateProvider.getTemplates");
        return templates;
    }

    @Override
    public boolean isTerminalSymbol(char c) {
        return c == '.';
    }

    @Override
    public void preExpand(@NotNull PsiFile psiFile, @NotNull Editor editor) {
        System.out.println("LogTemplateProvider.preExpand");
    }

    @Override
    public void afterExpand(@NotNull PsiFile psiFile, @NotNull Editor editor) {
        System.out.println("LogTemplateProvider.afterExpand");
    }

    @NotNull
    @Override
    public PsiFile preCheck(@NotNull PsiFile psiFile, @NotNull Editor editor, int i) {
        return psiFile;
    }
}
