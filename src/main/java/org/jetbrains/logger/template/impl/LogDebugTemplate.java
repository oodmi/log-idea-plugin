package org.jetbrains.logger.template.impl;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

import org.jetbrains.logger.LogTemplateProvider;
import org.jetbrains.logger.template.LogTemplate;

public class LogDebugTemplate extends LogTemplate {
    public LogDebugTemplate(LogTemplateProvider logTemplateProvider) {
        super("logd",
              LOGGER + ".debug(expr)",
              "debug",
              logTemplateProvider);
    }
}
