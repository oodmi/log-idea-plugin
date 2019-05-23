package org.jetbrains.logger;

import java.io.File;

public class LoggerInfoTemplateTest extends LoggerTestCase {

    @Override
    protected String getTestDataPath() {
        return new File("testdata").getAbsolutePath();
    }

    public void testParentIsPsiExpressionStatement1() {
        doTest();
    }

    public void testParentIsPsiLocalVariable2() {
        doTest();
    }

    public void testParentIsPsiExpressionStatementAndElementIsPsiMethodCallExpression3() {
        doTest();
    }

    public void testParentIsPsiLocalVariableAndElementIsPsiMethodCallExpression4() {
        doTest();
    }

    public void testParentIsPsiReturnStatement5() {
        doTest();
    }

    public void testParentIsPsiAssignmentExpression6() {
        doTest();
    }

    public void testParentIsPsiExpressionList7() {
        doTest();
    }

    public void testParentIsPsiExpressionListAndElementIsPsiMethodCallExpression8() {
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

    public void testParentIsPsiExpressionListAndElementIsPsiMethodCallExpressionAndWithAssignment11() {
        doTest();
    }

    public void testVoidMethod12() {
        doTest();
    }

    public void testElementIsPsiReferenceExpression13() {
        doTest();
    }

    public void testParentIsPsiExpressionStatementAndElementIsPsiReferenceExpression14() {
        doTest();
    }

    public void testParentIsPsiExpressionListAndElementIsPsiNewExpression15() {
        doTest();
    }

}