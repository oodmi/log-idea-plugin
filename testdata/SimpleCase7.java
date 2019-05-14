package com.oodmi.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerTest {

    public String method1() {
        method2("1".logi<caret>);
    }

    public void method2(String s){

    }
}
