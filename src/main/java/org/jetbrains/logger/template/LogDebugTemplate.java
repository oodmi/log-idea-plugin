package org.jetbrains.logger.template;

public class LogDebugTemplate extends LogTemplate {
    public LogDebugTemplate() {
        super("logd", "%logger%.debug(expr);");
    }
}
