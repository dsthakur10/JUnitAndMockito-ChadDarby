package com.devendra.test;

import com.devendra.component.MvcTestingExampleApplication;
import com.devendra.component.models.CollegeStudent;
import com.devendra.component.models.StudentGrades;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ReflectionTestUtilsTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

    @BeforeEach
    public void beforeEach() {
        student.setFirstname("Devendra");
        student.setLastname("Thakur");
        student.setEmailAddress("devthakur@gmail.com");
        student.setStudentGrades(studentGrades);

        ReflectionTestUtils.setField(student, "id", 10);
        ReflectionTestUtils.setField(student, "studentGrades",
                new StudentGrades(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.5, 91.75))));

    }

    @Test
    @DisplayName("Get Private field data")
    void getPrivateField() {
        Assertions.assertEquals(10, ReflectionTestUtils.getField(student, "id"));
    }

    @Test
    @DisplayName("Invoke Private methods")
    void getPrivateMethod() {

        String nameId = "Devendra | 10";
        Assertions.assertEquals(nameId,
                ReflectionTestUtils.invokeMethod(student, "getFirstNameAndId"),
                "failed to load private method");
    }
}
