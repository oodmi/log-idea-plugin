package org.jetbrains.logger.template;

public class LogErrorTemplate extends LogTemplate {
    public LogErrorTemplate() {
        super("loge", "%logger%.error(expr);");
    }
}
