package com.oodmi.service;

@lombok.extern.slf4j.Slf4j
public class LoggerTest {

    public String method1() {
        Object o = new Object();
        String newVar = o.toString();
        log.info("newVar=" + newVar);
        method2(newVar);
    }

    public void method2(String s) {

    }
}

class Object {

    public String toString() {
        return "";
    }
}