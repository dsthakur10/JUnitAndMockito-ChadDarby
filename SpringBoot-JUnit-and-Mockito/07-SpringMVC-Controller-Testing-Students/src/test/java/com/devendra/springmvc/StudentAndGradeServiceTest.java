package com.devendra.springmvc;

import com.devendra.springmvc.models.CollegeStudent;
import com.devendra.springmvc.models.HistoryGrade;
import com.devendra.springmvc.models.MathGrade;
import com.devendra.springmvc.models.ScienceGrade;
import com.devendra.springmvc.repository.HistoryGradeDAO;
import com.devendra.springmvc.repository.MathGradeDAO;
import com.devendra.springmvc.repository.ScienceGradeDAO;
import com.devendra.springmvc.repository.StudentDAO;
import com.devendra.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySource("/application.properties")
public class StudentAndGradeServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentAndGradeService studentAndGradeService;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private MathGradeDAO mathGradeDAO;

    @Autowired
    private ScienceGradeDAO scienceGradeDAO;

    @Autowired
    private HistoryGradeDAO historyGradeDAO;

    @BeforeEach
    public void setup() {
        jdbc.execute("insert into student(id, firstname, lastname, email_address) " +
                "values(1, 'Leo', 'Messi', 'leomessi@football.com')");
    }

    @AfterEach
    public void cleanup() {
        jdbc.execute("DELETE FROM student");
    }


    @Test
    public void isStudentNullCheck() {
        Assertions.assertTrue(studentAndGradeService.isStudentPresent(1));
        Assertions.assertFalse(studentAndGradeService.isStudentPresent(0));
    }

    @Test
    public void createStudentService() {
        studentAndGradeService.createStudent("Devendra", "Thakur", "devt@gmail.com");

        CollegeStudent collegeStudent = studentDAO.findByEmailAddress("devt@gmail.com");

        Assertions.assertEquals("devt@gmail.com", collegeStudent.getEmailAddress(), "Find by Email");
    }

    @Test
    public void deleteStudentService() {
        Optional<CollegeStudent> deletedCollegeStudent = studentDAO.findById(1);
        Assertions.assertTrue(deletedCollegeStudent.isPresent(), "Return TRUE");

        studentAndGradeService.deleteStudent(1);

        deletedCollegeStudent = studentDAO.findById(1);
        Assertions.assertFalse(deletedCollegeStudent.isPresent(), "Return FALSE");
    }

    // @Sql annotation is used to set up DB from the given file --> Load & execute SQL for this given test
    @Test
    @Sql("/insertData.sql")
    public void getGradeBookService() {
        Iterable<CollegeStudent> collegeStudentIterable = studentAndGradeService.getGradebook();
        List<CollegeStudent> collegeStudents = new ArrayList<>();

        for(CollegeStudent collegeStudent: collegeStudentIterable)
            collegeStudents.add(collegeStudent);

        Assertions.assertEquals(6, collegeStudents.size());
    }


    // Tests for Grades

    @Test
    public void createGradeService() {

        // create the grade
        Assertions.assertTrue(studentAndGradeService.createGrade(80.5, 1, "math"));
        Assertions.assertTrue(studentAndGradeService.createGrade(99, 1, "science"));
        Assertions.assertTrue(studentAndGradeService.createGrade(77.7, 1, "history"));

        // Get all the grades with student ID
        Iterable<MathGrade> mathGrades = mathGradeDAO.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDAO.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDAO.findGradeByStudentId(1);

        // Verify there is a grade
        Assertions.assertTrue(mathGrades.iterator().hasNext(), "Student must have Math grades");
        Assertions.assertTrue(scienceGrades.iterator().hasNext(), "Student must have Science grades");
        Assertions.assertTrue(historyGrades.iterator().hasNext(), "Student must have History grades");

    }

    @Test
    public void createGradeServiceReturnFalse() {

        // invalid marks
        Assertions.assertFalse(studentAndGradeService.createGrade(111, 1, "math"));
        Assertions.assertFalse(studentAndGradeService.createGrade(-5.5, 1, "science"));

        // invalid student-id
        Assertions.assertFalse(studentAndGradeService.createGrade(80.5, 2, "history"));

        // invalid subject
        Assertions.assertFalse(studentAndGradeService.createGrade(80.5, 1, "english"));

    }

}

// What about the Database connection ?
/*
    - Our application.properties DB info is still commented out.
    - In SpringBoot, if an embedded DB is listed as a dependency, then SpringBoot will autoconfigure the DB connection
    - Our project has H2 dependency listed in pom.xml --> SpringBoot has autoconfigured a connection to this embedded H2 DB
 */