package com.oodmi.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerTest {

    public String method1() {
        java.lang.String newVar = "1";
        log.info(newVar);
        return newVar;
    }
}
