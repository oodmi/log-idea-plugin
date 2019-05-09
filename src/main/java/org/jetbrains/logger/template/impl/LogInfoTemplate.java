package org.jetbrains.logger.template.impl;

import org.jetbrains.logger.LogTemplateProvider;
import org.jetbrains.logger.template.LogTemplate;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

public class LogInfoTemplate extends LogTemplate {
    public LogInfoTemplate(LogTemplateProvider logTemplateProvider) {
        super("logi",
                LOGGER + ".info(expr)",
                "info",
                logTemplateProvider);
    }
}
