package org.jetbrains.logger.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;

import java.util.Arrays;
import java.util.List;

public class LogUtils {

    public static final String LOGGER = "logger";
    public static final String PARENT = "parent";
    public static final String TYPE = "type";
    public static final String VAR = "var";

    private LogUtils() {

    }

    public static List<String> getPrimitives() {
        return Arrays.asList("Boolean",
                "Double",
                "Float",
                "Integer",
                "Short",
                "Byte",
                "Character");
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

    public static String getModifier() {
        return "static";
    }

    public static String replaceLast(String string, String from, String to) {
        int lastIndex = string.lastIndexOf(from);
        if (lastIndex < 0) return string;
        String substring = string.substring(lastIndex);
        String tail = substring.replace(from, to);
        return string.substring(0, lastIndex) + tail;
    }

    public static boolean isPrimitive(PsiElement element) {
        PsiExpression expression = (PsiExpression) element;
        final PsiType type = expression.getType();
        if (type instanceof PsiPrimitiveType) {
            return true;
        }
        try {
            String className = ((PsiClassReferenceType) type).getClassName();
            boolean isPrimitive = getPrimitives().contains(className);
            return isPrimitive;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNeedBraces(PsiElement element) {
        PsiExpression expression = (PsiExpression) element;
        final PsiType type = expression.getType();
        if (type instanceof PsiPrimitiveType) {
            return true;
        }
        try {
            String className = ((PsiClassReferenceType) type).getClassName();
            boolean need = !"String".equals(className);
            return need;
        } catch (Exception e) {
            return true;
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
}
