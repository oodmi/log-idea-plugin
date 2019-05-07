package org.jetbrains.logger.utils;

import java.util.Arrays;
import java.util.List;

public class LogUtils {

    public static final String LOGGER = "logger";

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
}
