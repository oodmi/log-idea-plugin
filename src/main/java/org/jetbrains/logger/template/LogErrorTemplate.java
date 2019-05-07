package org.jetbrains.logger.template;

import org.jetbrains.logger.LogTemplateProvider;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

public class LogErrorTemplate extends LogTemplate {
    public LogErrorTemplate(LogTemplateProvider logTemplateProvider) {
        super("loge",
                LOGGER + ".error(expr)",
                "$" + LOGGER + "$.error($expr$);$END$",
                logTemplateProvider);

    }
}

