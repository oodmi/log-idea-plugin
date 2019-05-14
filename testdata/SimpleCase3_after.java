package com.oodmi.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerTest {

    public void method1() {
        log.info(method2());
    }

    public String method2() {
        return "1";
    }
}
