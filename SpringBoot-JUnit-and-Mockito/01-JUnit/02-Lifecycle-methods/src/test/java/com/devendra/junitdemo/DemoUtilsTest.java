package com.devendra.junitdemo;

import org.junit.jupiter.api.*;

public class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeAll
    static void setupBeforeAll() {
        System.out.println("@BeforeALL executes only ONCE BEFORE the execution of all test methods in the class");
    }

    @AfterAll
    static void teardownAfterAll() {
        System.out.println("@AfterALL executes only ONCE AFTER the execution of all test methods in the class");
    }

    @BeforeEach
    void setupBeforeEachTest() {
        demoUtils = new DemoUtils();
        System.out.println("@BeforeEACH executes BEFORE the execution of each test");
    }

    @AfterEach
    void teardownAfterEachTest(){
        System.out.println("@AfterEACH executes AFTER the execution of each test");
    }


    @Test
    void testEqualsAndNotEquals() {
        System.out.println("Running Test: testEqualsAndNotEquals()");
        Assertions.assertEquals(6, demoUtils.add(2,4), "2 + 4 must be 6");
        Assertions.assertNotEquals(11, demoUtils.add(1,9), "1 + 9 must not be 10");
    }

    @Test
    void testNullAndNotNull() {
        System.out.println("Running Test: testNullAndNotNull()");
        Assertions.assertNull(demoUtils.checkNull(null), "Object should be NULL");
        Assertions.assertNotNull(demoUtils.checkNull("Leo Messi"), "Object should NOT be NULL");
    }
}
