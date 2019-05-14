package org.jetbrains.logger;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

abstract public class LoggerTestCase extends LightCodeInsightFixtureTestCase {
    protected void doTest() {
        myFixture.configureByFile(getTestName(false) + ".java");
        myFixture.type('\t');
        myFixture.checkResultByFile(getTestName(false) + "_after.java", true);
    }
}
