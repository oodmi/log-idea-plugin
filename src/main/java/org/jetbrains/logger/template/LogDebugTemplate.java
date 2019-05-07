package org.jetbrains.logger.template;

import org.jetbrains.logger.LogTemplateProvider;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

public class LogDebugTemplate extends LogTemplate {
    public LogDebugTemplate(LogTemplateProvider logTemplateProvider) {
        super("logd",
                LOGGER + ".debug(expr)",
                "$" + LOGGER + "$.debug($expr$);$END$",
                logTemplateProvider);
    }
}
