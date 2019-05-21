package com.oodmi.service;

@lombok.extern.slf4j.Slf4j
public class LoggerTest {

    public String method1() {
        String s = "1";
        log.info(s);
    }

    public String method2(String s){
        return s;
    }

    public void method3(String s){

    }
}
