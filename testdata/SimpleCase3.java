package com.oodmi.service;

@lombok.extern.slf4j.Slf4j
public class LoggerTest {

    public void method1() {
        method2().logi<caret>;
    }

    public String method2() {
        return "1";
    }
}
