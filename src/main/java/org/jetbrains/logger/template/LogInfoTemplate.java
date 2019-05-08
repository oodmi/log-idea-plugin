package org.jetbrains.logger.template;

import org.jetbrains.logger.LogTemplateProvider;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

public class LogInfoTemplate extends LogTemplate {
    public LogInfoTemplate(LogTemplateProvider logTemplateProvider) {
        super("logi",
                LOGGER + ".info(expr)",
                "info",
                logTemplateProvider);
    }
}
