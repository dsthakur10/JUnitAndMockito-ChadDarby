package com.devendra.junitdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DemoUtilsTest {

    @Test
    void testEqualsAndNotEquals() {

        DemoUtils demoUtils = new DemoUtils();
        Assertions.assertEquals(6, demoUtils.add(2,4), "2 + 4 must be 6");
        Assertions.assertNotEquals(11, demoUtils.add(1,9), "1 + 9 must not be 10");
    }

    @Test
    void testNullAndNotNull() {
        DemoUtils demoUtils = new DemoUtils();

        Assertions.assertNull(demoUtils.checkNull(null), "Object should be NULL");
        Assertions.assertNotNull(demoUtils.checkNull("Leo Messi"), "Object should NOT be NULL");
    }
}
