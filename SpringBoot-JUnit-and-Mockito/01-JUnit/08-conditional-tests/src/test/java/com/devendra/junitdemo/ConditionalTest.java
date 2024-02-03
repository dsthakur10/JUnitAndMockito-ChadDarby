package com.devendra.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

public class ConditionalTest {

    @Test
    @Disabled("Don't run until JIRA #451 is resolved")
    void basicTest() {
        // execute method & perform assets
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindowsOnly() {
        // execute method & perform assets
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testForMacOnly() {
        // execute method & perform assets
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void testForMacAndLinuxOnly() {
        // execute method & perform assets
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void testForJava8() {
        // execute method & perform assets
    }

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testForJava17() {
        // execute method & perform assets
    }

    @Test
    @EnabledOnJre(JRE.JAVA_21)
    void testForJava21() {
        // execute method & perform assets
    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_13, max=JRE.JAVA_20)
    void testForJavaRange() {
        // execute method & perform assets
    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_13)
    void testForJavaMin() {
        // execute method & perform assets
    }

    @Test
    @EnabledIfEnvironmentVariable(named="MESSI_ENV", matches = "DEV")
    void testOnlyForDevEnvironment() {
        // execute method & perform assets
    }

    @Test
    @EnabledIfSystemProperty(named="MESSI_SYS_PROP", matches = "CI_CD_DEPLOY")
    void testOnlyForCiCdDeployProperty() {
        // execute method & perform assets
    }

}
