package org.jetbrains.logger;

import java.io.File;

public class LoggerInfoTemplateTest extends LoggerTestCase {

    @Override
    protected String getTestDataPath() {
        return new File("testdata").getAbsolutePath();
    }

    public void testSimpleCase() {
        doTest();
    }

}