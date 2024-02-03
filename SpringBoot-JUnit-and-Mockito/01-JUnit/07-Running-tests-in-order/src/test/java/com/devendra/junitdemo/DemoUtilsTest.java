package com.devendra.junitdemo;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEachTest() {
        demoUtils = new DemoUtils();
    }

    @Test
    @Order(2)
    void testEqualsAndNotEquals() {
        Assertions.assertEquals(6, demoUtils.add(2,4), "2 + 4 must be 6");
        Assertions.assertNotEquals(11, demoUtils.add(1,9), "1 + 9 must not be 10");
    }

    @Test
    @Order(-5)
    void testNullAndNotNull() {
        Assertions.assertNull(demoUtils.checkNull(null), "Object should be NULL");
        Assertions.assertNotNull(demoUtils.checkNull("Leo Messi"), "Object should NOT be NULL");
    }

    @Test
    @Order(-111)
    void testSameAndNotSame() {
        String str = "LEO-MESSI";

        Assertions.assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate(), "Objects must refer to same object");
        Assertions.assertNotSame(str, demoUtils.getAcademy(), "Objects must NOT refer to same object");
    }

    @Test
    @Order(200)
    void testTrueAndFalse() {
        Assertions.assertTrue(demoUtils.isGreater(50, 25), "First > second is needed");
        Assertions.assertFalse(demoUtils.isGreater(22, 22), "First <= second is needed");
    }

    @Test
    @Order(100)
    void testArrayEquals() {
        String[] arr = {"A", "B", "C"};

        Assertions.assertArrayEquals(arr, demoUtils.getFirstThreeLettersOfAlphabet(), "Arrays should be same");
    }

    @Test
    @Order(0)
    void testIterableEquals() {
        List<String> theList = List.of("luv", "2", "code");

        Assertions.assertIterableEquals(theList, demoUtils.getAcademyInList(), "Expected List should be same");
    }

    @Test
    @Order(55)
    void testLinesMatch() {
        List<String> theList = List.of("luv", "2", "code");

        Assertions.assertLinesMatch(theList, demoUtils.getAcademyInList(), "Lines should match");
    }

    @Test
    @Order(-77)
    void testThrowsAndDoesNotThrow() {
        Assertions.assertThrows(Exception.class, () -> { demoUtils.throwException(-5); }, "Should throw an exception");
        Assertions.assertDoesNotThrow(() -> { demoUtils.throwException(10); }, "Should NOT throw exception");
    }

    @Test
    @Order(11)
    void testTimeout() {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {demoUtils.checkTimeout();} , "Should terminate within 3 seconds");
    }
}
