package org.jetbrains.logger;

import java.io.File;

public class LoggerInfoTemplateTest extends LoggerTestCase {

    @Override
    protected String getTestDataPath() {
        return new File("testdata").getAbsolutePath();
    }

    public void testSimpleCase1() {
        doTest();
    }

    public void testSimpleCase2() {
        doTest();
    }

    public void testSimpleCase3() {
        doTest();
    }

    public void testSimpleCase4() {
        doTest();
    }

    public void testSimpleCase6() {
        doTest();
    }

    public void testSimpleCase7() {
        doTest();
    }

    public void testSimpleCase8() {
        doTest();
    }

    public void testFieldLogger9() {
        doTest();
    }

    public void testWithoutLogger10() {
        myFixture.configureByFile(getTestName(false) + ".java");
        try{
            myFixture.type('\t');
            myFixture.completeBasic();
        }catch (Exception e){
            myFixture.checkResultByFile(getTestName(false) + "_after.java", true);
        }

    }

    public void testSimpleCase11() {
        doTest();
    }

    public void testVoidMethod12() {
        doTest();
    }

    public void testSimpleCase13() {
        doTest();
    }

}