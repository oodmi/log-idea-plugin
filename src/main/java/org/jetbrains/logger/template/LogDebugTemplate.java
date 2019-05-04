package org.jetbrains.logger.template;

import org.jetbrains.logger.LogTemplateProvider;

public class LogDebugTemplate extends LogTemplate {
    public LogDebugTemplate(LogTemplateProvider logTemplateProvider) {
        super("logd", "logger.debug(expr)", "$logger$.debug($expr$);$END$", logTemplateProvider);
    }
}
