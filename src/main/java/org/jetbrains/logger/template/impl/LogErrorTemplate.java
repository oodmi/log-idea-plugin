package org.jetbrains.logger.template.impl;

import static org.jetbrains.logger.utils.LogUtils.LOGGER;

import org.jetbrains.logger.LogTemplateProvider;
import org.jetbrains.logger.template.LogTemplate;

public class LogErrorTemplate extends LogTemplate {
    public LogErrorTemplate(LogTemplateProvider logTemplateProvider) {
        super("loge",
              LOGGER + ".error(expr)",
              "error",
              logTemplateProvider);

    }
}

