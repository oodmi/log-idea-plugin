package org.jetbrains.logger.utils;

import com.intellij.lang.jvm.JvmModifier;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LogUtils {

    public static final String LOGGER = "logger";
    public static final String TYPE = "type";
    public static final String VAR = "var";
    public static final String CURSOR = "cursor";

    private LogUtils() {
    }

    public static List<String> getLombok() {
        return Arrays.asList(
                "lombok.extern.apachecommons.CommonsLog", "CommonsLog",
                "lombok.extern.java.Log", "Log",
                "lombok.extern.log4j.Log4j", "Log4j",
                "lombok.extern.log4j.Log4j2", "Log4j2",
                "lombok.extern.slf4j.XSlf4j", "XSlf4j",
                "lombok.extern.jbosslog.JBossLog", "JBossLog",
                "lombok.extern.flogger.Flogger", "Flogger",
                "lombok.extern.slf4j.Slf4j", "Slf4j");
    }

    public static List<String> getLoggers() {
        return Arrays.asList("log", "logger", "logging", "lgr");
    }

    public static String replaceLast(String string, String from, String to) {
        int lastIndex = string.lastIndexOf(from);
        if (lastIndex < 0) return string;
        String substring = string.substring(lastIndex);
        String tail = substring.replace(from, to);
        return string.substring(0, lastIndex) + tail;
    }

    public static boolean isTypeString(PsiElement element) {
        PsiExpression expression = (PsiExpression) element;
        final PsiType type = expression.getType();
        try {
            String className = ((PsiClassReferenceType) type).getClassName();
            return "String".equals(className);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getLombokName() {
        //TODO: calculate name from lombok.config
        return "log";
    }

    public static PsiElement getParent(PsiElement psiElement) {
        PsiElement parent = psiElement;
        while (!parent.getText().endsWith(";")) {
            parent = parent.getParent();
        }
        return parent;
    }

    public static String getLoggerName(@NotNull PsiElement element) {
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        if (Objects.isNull(psiClass)) {
            return null;
        }
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            String qualifiedName = annotation.getQualifiedName();
            if (qualifiedName != null && getLombok().contains(qualifiedName)) {
                return getLombokName();
            }
        }
        PsiField[] allFields = psiClass.getAllFields();
        for (PsiField field : allFields) {
            if (field.hasModifier(JvmModifier.STATIC)) {
                String clazz = field.getType().getCanonicalText();
                boolean suitable = getLoggers().stream().anyMatch(it -> clazz.toLowerCase().contains(it));
                if (suitable) {
                    return field.getName();
                }
            }
        }
        return null;
    }

    public static void showErrorHint(@NotNull Project project, @NotNull Editor editor) {
        CommonRefactoringUtil.showErrorHint(project,
                                            editor,
                                            "Can't find logger",
                                            "Can't find logger",
                                            "");
    }
}
