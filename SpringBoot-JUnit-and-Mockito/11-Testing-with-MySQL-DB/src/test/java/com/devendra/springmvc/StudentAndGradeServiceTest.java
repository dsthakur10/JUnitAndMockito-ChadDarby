package com.devendra.springmvc;

import com.devendra.springmvc.models.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySource("/application-test.properties")
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

    // Inject SQL scripts from application.properties
    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;


    @BeforeEach
    public void setup() {
        jdbc.execute(sqlAddStudent);

        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
    }

    @AfterEach
    public void cleanup() {
        jdbc.execute(sqlDeleteStudent);

        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }


    // Tests for Students

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

        // Delete a student & all its grades

        Optional<CollegeStudent> deletedCollegeStudent = studentDAO.findById(1);
        Optional<MathGrade> deletedMathGrade = mathGradeDAO.findById(1);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradeDAO.findById(1);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradeDAO.findById(1);

        Assertions.assertTrue(deletedCollegeStudent.isPresent(), "Return TRUE");
        Assertions.assertTrue(deletedMathGrade.isPresent(), "Return true");
        Assertions.assertTrue(deletedScienceGrade.isPresent(), "Return true");
        Assertions.assertTrue(deletedHistoryGrade.isPresent(), "Return true");

        studentAndGradeService.deleteStudent(1);

        deletedCollegeStudent = studentDAO.findById(1);
        deletedMathGrade = mathGradeDAO.findById(1);
        deletedScienceGrade = scienceGradeDAO.findById(1);
        deletedHistoryGrade = historyGradeDAO.findById(1);

        Assertions.assertFalse(deletedCollegeStudent.isPresent(), "Return FALSE");
        Assertions.assertFalse(deletedMathGrade.isPresent(), "Return false");
        Assertions.assertFalse(deletedScienceGrade.isPresent(), "Return false");
        Assertions.assertFalse(deletedHistoryGrade.isPresent(), "Return false");

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

        // Verify the exact count of grades
        Assertions.assertEquals(2, ((Collection<ScienceGrade>) scienceGrades).size(),
                "Student must have Science grades");
        Assertions.assertEquals(2, ((Collection<HistoryGrade>) historyGrades).size(),
                "Student must have History grades");
    }

    @Test
    public void createGradeServiceInvalid() {

        // invalid marks
        Assertions.assertFalse(studentAndGradeService.createGrade(111, 1, "math"));
        Assertions.assertFalse(studentAndGradeService.createGrade(-5.5, 1, "science"));

        // invalid student-id
        Assertions.assertFalse(studentAndGradeService.createGrade(80.5, 2, "history"));

        // invalid subject
        Assertions.assertFalse(studentAndGradeService.createGrade(80.5, 1, "english"));
    }

    @Test
    public void deleteGradeService() {

        Assertions.assertEquals(1, studentAndGradeService.deleteGrade(1, "math"),
                "Returns student-id after delete");
        Assertions.assertEquals(1, studentAndGradeService.deleteGrade(1, "science"),
                "Returns student-id after delete");
        Assertions.assertEquals(1, studentAndGradeService.deleteGrade(1, "history"),
                "Returns student-id after delete");
    }

    @Test
    public void deleteGradeServiceInvalid() {

        // Invalid Grade-ID
        Assertions.assertEquals(0, studentAndGradeService.deleteGrade(0, "math"),
                "Returns student-id after delete");

        // Invalid subject
        Assertions.assertEquals(0, studentAndGradeService.deleteGrade(1, "english"),
                "Returns student-id after delete");

    }

    @Test
    public void studentInformationService() {

        GradebookCollegeStudent gradebookCollegeStudent = studentAndGradeService.studentInformation(1);

        Assertions.assertNotNull(gradebookCollegeStudent, "Student must exist");
        Assertions.assertEquals(1, gradebookCollegeStudent.getId());
        Assertions.assertEquals("Leo", gradebookCollegeStudent.getFirstname());
        Assertions.assertEquals("Messi", gradebookCollegeStudent.getLastname());
        Assertions.assertEquals("leomessi@football.com", gradebookCollegeStudent.getEmailAddress());
        Assertions.assertEquals(1, gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size());
        Assertions.assertEquals(1, gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size());
        Assertions.assertEquals(1, gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size());
    }

    @Test
    public void studentInformationServiceInvalid() {

        GradebookCollegeStudent gradebookCollegeStudent = studentAndGradeService.studentInformation(0);

        Assertions.assertNull(gradebookCollegeStudent, "Student must NOT exist");
    }
}

// What about the Database connection ?
/*
    - Our application.properties DB info is still commented out.
    - In SpringBoot, if an embedded DB is listed as a dependency, then SpringBoot will autoconfigure the DB connection
    - Our project has H2 dependency listed in pom.xml --> SpringBoot has autoconfigured a connection to this embedded H2 DB
 */