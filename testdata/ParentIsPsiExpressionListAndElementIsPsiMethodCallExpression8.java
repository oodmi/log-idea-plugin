package com.oodmi.service;

@lombok.extern.slf4j.Slf4j
public class LoggerTest {

    public String method1() {
        Object o = new Object();
        method2(o.toString().logi<caret>);
    }

    public void method2(String s) {

    }
}

class Object {

    public String toString() {
        return "";
    }
}