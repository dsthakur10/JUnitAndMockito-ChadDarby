package com.devendra.test;

import com.devendra.component.MvcTestingExampleApplication;
import com.devendra.component.models.CollegeStudent;
import com.devendra.component.models.StudentGrades;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ApplicationExampleTest {

    private static int count = 0;

    @Value("${info.app.name}")
    private String appInfo;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.school.name}")
    private String schoolName;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    public void beforeEachMethod() {
        count++;

        System.out.println("Testing: " + appInfo + " which is " + appDescription +
                " Version: " + appVersion + ". Execution of test method = " + count);
        student.setFirstname("Leo");
        student.setLastname("Messi");
        student.setEmailAddress("leomessi@football.com");
        studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.5, 91.75)));
        student.setStudentGrades(studentGrades);
    }


    @Test
    @DisplayName("ADD Grade results for Students grades")
    void addGradeResultsForStudentsGrades() {
        Assertions.assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()
        ));
    }

    @Test
    @DisplayName("ADD Grade results for Students grades NOT Equal")
    void addGradeResultsForStudentsGradesAssertNotEquals() {
        Assertions.assertNotEquals(100, studentGrades.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()
        ));
    }

    @Test
    @DisplayName("Is Grade Greater")
    void isGradeGreater() {
        Assertions.assertTrue(studentGrades.isGradeGreater(90, 77),
                "Failure - should be true");
    }

    @Test
    @DisplayName("Is Grade Greater FALSE")
    void isGradeGreaterFalse() {
        Assertions.assertFalse(studentGrades.isGradeGreater(90, 100),
                "Failure - should be false");
    }

    @Test
    @DisplayName("Check NULL for student grades")
    void checkNullForStudentGrades() {
        Assertions.assertNotNull(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()),
                "Object should not be NULL");
    }

    @Test
    @DisplayName("Create Student without Grade init")
    void createStudentWithoutGradeInit() {

        CollegeStudent s2 = context.getBean("collegeStudent", CollegeStudent.class);

        s2.setFirstname("Cristiano");
        s2.setLastname("Ronaldo");
        s2.setEmailAddress("ronaldo7@football.com");

        Assertions.assertNotNull(s2.getFirstname(), "First Name must not be NULL");
        Assertions.assertNotNull(s2.getLastname(), "Last Name must not be NULL");
        Assertions.assertNotNull(s2.getEmailAddress(), "Email must not be NULL");
        Assertions.assertNull(s2.getStudentGrades(), "Grades should be NULL");
    }

    @Test
    @DisplayName("Verify Student objects are prototypes")
    void verifyStudentsArePrototypes() {
        CollegeStudent s3 = context.getBean("collegeStudent", CollegeStudent.class);
        CollegeStudent s4 = context.getBean("collegeStudent", CollegeStudent.class);

        Assertions.assertNotSame(s3, s4, "Objects CANNOT be same");
    }

    @Test
    @DisplayName("Find Grade Point Average")
    void findGradePointAverage() {
        Assertions.assertAll("Testing all AssertEquals",
                () -> Assertions.assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(
                        student.getStudentGrades().getMathGradeResults())),
                () -> Assertions.assertEquals(88.31, studentGrades.findGradePointAverage(
                        student.getStudentGrades().getMathGradeResults()))
        );
    }
}
