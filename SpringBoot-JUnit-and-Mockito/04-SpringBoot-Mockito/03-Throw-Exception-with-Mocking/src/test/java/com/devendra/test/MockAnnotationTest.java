package com.devendra.test;

import com.devendra.component.MvcTestingExampleApplication;
import com.devendra.component.dao.ApplicationDao;
import com.devendra.component.models.CollegeStudent;
import com.devendra.component.models.StudentGrades;
import com.devendra.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

//    @Mock
//    private ApplicationDao applicationDao;

    @MockBean
    private ApplicationDao applicationDao;

//    @InjectMocks
//    private ApplicationService applicationService;

    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach() {
        student.setFirstname("Devendra");
        student.setLastname("Thakur");
        student.setEmailAddress("devthakur@gmail.com");
        student.setStudentGrades(studentGrades);
    }

    @Test
    @DisplayName("When & Verify")
    public void testAssertEqualsTestGrades() {
        when(applicationDao.addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults())).thenReturn(100.0);

        assertEquals(100, applicationService.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()));

        // verify if a particular mock method is called during testing process
        verify(applicationDao).
                addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

        // verify how many times has a particular mock method is called during testing process
        verify(applicationDao, times(1)).
                addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

        //System.out.println(student.toString());

    }

    @Test
    @DisplayName("Find GPA")
    public void testAssertEqualsFindGPA() {
        when(applicationDao.findGradePointAverage(
                studentGrades.getMathGradeResults())).thenReturn(88.31);

        assertEquals(88.31, applicationService.
                findGradePointAverage(student.getStudentGrades().getMathGradeResults()));

        verify(applicationDao, times(1)).
                findGradePointAverage(studentGrades.getMathGradeResults());
    }

    @Test
    @DisplayName("Not Null")
    void testAssertNotNull() {
        when(applicationDao.checkNull(
                studentGrades.getMathGradeResults())).thenReturn(true);

        assertNotNull(applicationService.
                checkNull(student.getStudentGrades().getMathGradeResults()), "Object should not be NULL");
    }

    @Test
    @DisplayName("Throw Runtime error")
    void testThrowRuntimeError() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        when(applicationDao.checkNull(nullStudent)).
                thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            applicationService.checkNull(nullStudent);
        });

        verify(applicationDao, times(1)).checkNull(nullStudent);
    }

    @Test
    @DisplayName("Multiple calling to same method that throws exception")
    void stubbingConsecutiveCalls() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
        String message = "Do not Throw Exceptions. Multiple calls occurred";

        when(applicationDao.checkNull(nullStudent)).
                thenThrow(new RuntimeException()).
                thenReturn(message);

        assertThrows(RuntimeException.class, () -> {
            applicationService.checkNull(nullStudent);
        });

        assertEquals(message, applicationService.checkNull(nullStudent), "Different message received");

        verify(applicationDao, times(2)).checkNull(nullStudent);
    }

}
