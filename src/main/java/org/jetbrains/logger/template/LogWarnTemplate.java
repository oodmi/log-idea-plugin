package org.jetbrains.logger.template;

import org.jetbrains.logger.LogTemplateProvider;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

public class LogWarnTemplate extends LogTemplate {
    public LogWarnTemplate(LogTemplateProvider logTemplateProvider) {
        super("logw",
                LOGGER + ".warn(expr)",
                "trace",
                logTemplateProvider);
    }
}
