package com.devendra.junitdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEachTest() {
        demoUtils = new DemoUtils();
    }

    @Test
    void testEqualsAndNotEquals() {
        Assertions.assertEquals(6, demoUtils.add(2,4), "2 + 4 must be 6");
        Assertions.assertNotEquals(11, demoUtils.add(1,9), "1 + 9 must not be 10");
    }

    @Test
    void testNullAndNotNull() {
        Assertions.assertNull(demoUtils.checkNull(null), "Object should be NULL");
        Assertions.assertNotNull(demoUtils.checkNull("Leo Messi"), "Object should NOT be NULL");
    }

    @Test
    void sameAndNotSame() {
        String str = "LEO-MESSI";

        Assertions.assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate(), "Objects must refer to same object");
        Assertions.assertNotSame(str, demoUtils.getAcademy(), "Objects must NOT refer to same object");
    }

    @Test
    void trueAndFalse() {
        Assertions.assertTrue(demoUtils.isGreater(50, 25), "First > second is needed");
        Assertions.assertFalse(demoUtils.isGreater(22, 22), "First <= second is needed");
    }
}
