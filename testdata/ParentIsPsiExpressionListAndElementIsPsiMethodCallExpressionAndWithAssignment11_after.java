package com.oodmi.service;

@lombok.extern.slf4j.Slf4j
public class LoggerTest {

    public String method1() {
        Object o = new Object();
        String newVar = o.toString();
        log.info("newVar=" + newVar);
        String i = method2(newVar);
    }

    public String method2(String s) {
        return "";
    }
}

class Object {

    public String toString() {
        return "";
    }
}