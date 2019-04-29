package org.jetbrains.logger.template;

public class LogInfoTemplate extends LogTemplate {
    public LogInfoTemplate() {
        super("logi", "logger.info(expr)", "$logger$.info($expr$);$END$");
    }
}
