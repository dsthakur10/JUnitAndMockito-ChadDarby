package com.devendra.springmvc;

import com.devendra.springmvc.models.CollegeStudent;
import com.devendra.springmvc.models.GradebookCollegeStudent;
import com.devendra.springmvc.repository.StudentDAO;
import com.devendra.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
public class GradebookControllerTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentDAO studentDAO;

    @Mock
    private StudentAndGradeService studentCreateServiceMock;

    private static MockHttpServletRequest req;

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

    
    @BeforeAll
    public static void initialSetup() {
        req = new MockHttpServletRequest();
        req.setParameter("firstname", "Jasmine");
        req.setParameter("lastname", "Rao");
        req.setParameter("emailAddress", "Jasmine@love.com");
    }


    // Test for getting a list of students
    @Test
    public void getStudentHttpRequest() throws Exception {

        CollegeStudent collegeStudent1 = new GradebookCollegeStudent("Leo", "Messi", "leomessi@football.com");
        CollegeStudent collegeStudent2 = new GradebookCollegeStudent("Devendra", "Thakur", "devt@gmail.com");
        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(collegeStudent1, collegeStudent2));

        when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);
        Assertions.assertEquals(collegeStudentList, studentCreateServiceMock.getGradebook(), "College students list must be same");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");

        // Controller returns "index" as view
    }

    // Test for creating a Student
    @Test
    public void createStudentHttpRequest() throws Exception {

        CollegeStudent collegeStudent1 = new GradebookCollegeStudent("Leo", "Messi", "leomessi@football.com");
        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(collegeStudent1));

        when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);
        Assertions.assertEquals(collegeStudentList, studentCreateServiceMock.getGradebook(), "College students list must be same");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", req.getParameterValues("firstname"))
                .param("lastname", req.getParameterValues("lastname"))
                .param("emailAddress", req.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");

        CollegeStudent verifyStudent = studentDAO.findByEmailAddress("Jasmine@love.com");
        Assertions.assertNotNull(verifyStudent, "Student should NOT be NULL");

    }

    // Test for deleting a student
    @Test
    public void deleteStudentHttpRequest() throws Exception {

        Assertions.assertTrue(studentDAO.findById(1).isPresent(), "Student must exists");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");

        Assertions.assertFalse(studentDAO.findById(1).isPresent(), "Student must NOT exist");

    }

    // Test for deleting a non-existing student
    @Test
    public void deleteStudentHttpRequestErrorPage() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/delete/student/{id}", 0))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "error");

    }
}
