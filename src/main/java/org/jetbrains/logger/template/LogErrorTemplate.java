package org.jetbrains.logger.template;

import org.jetbrains.logger.LogTemplateProvider;

public class LogErrorTemplate extends LogTemplate {
    public LogErrorTemplate(LogTemplateProvider logTemplateProvider) {
        super("loge", "logger.error(expr)", "$logger$.error($expr$);$END$", logTemplateProvider);
    }
}
