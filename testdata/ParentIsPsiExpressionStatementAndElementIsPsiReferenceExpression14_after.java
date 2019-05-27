package com.oodmi.service;

@lombok.extern.slf4j.Slf4j
public class LoggerTest {

    public String method1() {
        String s = "1";
        log.info("s=" + s);
    }
}
