package org.jetbrains.logger.template;

import org.jetbrains.logger.LogTemplateProvider;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

public class LogTraceTemplate extends LogTemplate {
    public LogTraceTemplate(LogTemplateProvider logTemplateProvider) {
        super("logt",
                LOGGER + ".trace(expr)",
                "trace",
                logTemplateProvider);
    }
}
