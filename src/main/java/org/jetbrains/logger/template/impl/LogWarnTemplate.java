package org.jetbrains.logger.template.impl;

import org.jetbrains.logger.LogTemplateProvider;
import org.jetbrains.logger.template.LogTemplate;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

public class LogWarnTemplate extends LogTemplate {
    public LogWarnTemplate(LogTemplateProvider logTemplateProvider) {
        super("logw",
                LOGGER + ".warn(expr)",
                "trace",
                logTemplateProvider);
    }
}
