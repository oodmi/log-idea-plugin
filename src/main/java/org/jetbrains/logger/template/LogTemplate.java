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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.IS_NON_VOID;
import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.selectorAllExpressionsWithCurrentOffset;

public class LogTemplate extends StringBasedPostfixTemplate {

    private String templateString;

    protected LogTemplate(@NotNull String name, @NotNull String example, @NotNull String templateString) {
        super(name, example, selectorAllExpressionsWithCurrentOffset(IS_NON_VOID));
        this.templateString = templateString;
    }

    public static List<String> getLombok() {
        return Arrays.asList("lombok.extern.apachecommons.CommonsLog",
                "lombok.extern.java.Log",
                "lombok.extern.log4j.Log4j",
                "lombok.extern.log4j.Log4j2",
                "lombok.extern.slf4j.XSlf4j",
                "lombok.extern.jbosslog.JBossLog",
                "lombok.extern.flogger.Flogger",
                "lombok.extern.slf4j.Slf4j");
    }

    public static List<String> getLoggers() {
        return Arrays.asList("log", "logger", "logging", "lgr");
    }

    public static String getModifier() {
        return "static";
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
            if (qualifiedName != null && getLombok().contains(qualifiedName)) {
                TextExpression log = new TextExpression("log");
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
}
