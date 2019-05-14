package com.oodmi.service;

public class LoggerTest {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VkFriendHistoryService.class);

    public void method1() {
        logger.info("1");
    }
}