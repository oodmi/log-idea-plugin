package org.jetbrains.logger.template;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

import org.jetbrains.logger.LogTemplateProvider;

public class LogDebugTemplate extends LogTemplate {
    public LogDebugTemplate(LogTemplateProvider logTemplateProvider) {
        super("logd",
              LOGGER + ".debug(expr)",
              "debug",
              logTemplateProvider);
    }
}
