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

    @Mock
    private ApplicationDao applicationDao;

    @InjectMocks
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach() {
        student.setFirstname("Devendra");
        student.setLastname("Thakur");
        student.setEmailAddress("devthakur@gmail.com");
        student.setStudentGrades(studentGrades);
    }

    @DisplayName("When & Verify")
    @Test
    public void AssertEqualsTestGrades() {
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
}
