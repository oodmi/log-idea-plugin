package org.jetbrains.logger.template;

import org.jetbrains.logger.LogTemplateProvider;

public class LogInfoTemplate extends LogTemplate {
    public LogInfoTemplate(LogTemplateProvider logTemplateProvider) {
        super("logi", "logger.info(expr)", "$logger$.info($expr$);$END$", logTemplateProvider);
    }
}
