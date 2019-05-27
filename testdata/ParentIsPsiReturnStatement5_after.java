package com.oodmi.service;

@lombok.extern.slf4j.Slf4j
public class LoggerTest {

    public String method1() {
        java.lang.String newVar = "1";
        log.info("newVar=" + newVar);
        return newVar;
    }
}
