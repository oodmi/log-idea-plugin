package org.jetbrains.logger.template;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

import org.jetbrains.logger.LogTemplateProvider;

public class LogErrorTemplate extends LogTemplate {
    public LogErrorTemplate(LogTemplateProvider logTemplateProvider) {
        super("loge",
              LOGGER + ".error(expr)",
              "error",
              logTemplateProvider);

    }
}

