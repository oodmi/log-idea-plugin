package com.oodmi.service;

@lombok.extern.slf4j.Slf4j
public class LoggerTest {

    public void method1() {
        String i = method2();
        log.info(i);
    }

    public String method2() {
        return "1";
    }
}
