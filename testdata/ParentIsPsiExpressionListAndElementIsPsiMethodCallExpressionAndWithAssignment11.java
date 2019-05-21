package com.oodmi.service;

@lombok.extern.slf4j.Slf4j
public class LoggerTest {

    public String method1() {
        Object o = new Object();
        String i = method2(o.toString().logi<caret>);
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