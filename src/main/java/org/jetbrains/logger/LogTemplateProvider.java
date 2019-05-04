package org.jetbrains.logger;

import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.JavaCompletionContributor;
import com.intellij.codeInsight.template.postfix.templates.PostfixLiveTemplate;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateProvider;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.logger.template.LogDebugTemplate;
import org.jetbrains.logger.template.LogErrorTemplate;
import org.jetbrains.logger.template.LogInfoTemplate;

import java.util.Set;

public class LogTemplateProvider implements PostfixTemplateProvider {

    private final Set<PostfixTemplate> templates = ContainerUtil.newHashSet(
            new PostfixTemplate[]{
                    new LogInfoTemplate(this),
                    new LogDebugTemplate(this),
                    new LogErrorTemplate(this)
            });

    public LogTemplateProvider() {
        System.out.println("LogTemplateProvider.LogTemplateProvider");
    }

    private static boolean isSemicolonNeeded(@NotNull PsiFile file, @NotNull Editor editor) {
        return JavaCompletionContributor.semicolonNeeded(editor, file, CompletionInitializationContext.calcStartOffset(editor.getCaretModel().getCurrentCaret()));
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
        System.out.println("LogTemplateProvider.preCheck");
        Document document = psiFile.getViewProvider().getDocument();
        assert document != null;
        CharSequence sequence = document.getCharsSequence();
        StringBuilder fileContentWithSemicolon = new StringBuilder(sequence);
        if (isSemicolonNeeded(psiFile, editor)) {
            fileContentWithSemicolon.insert(i, ';');
            return PostfixLiveTemplate.copyFile(psiFile, fileContentWithSemicolon);
        }

        return psiFile;
    }
}
