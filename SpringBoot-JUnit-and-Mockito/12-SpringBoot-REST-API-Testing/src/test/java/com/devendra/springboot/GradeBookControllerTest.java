package com.devendra.springboot;

import com.devendra.springboot.models.CollegeStudent;
import com.devendra.springboot.models.MathGrade;
import com.devendra.springboot.repository.HistoryGradesDao;
import com.devendra.springboot.repository.MathGradesDao;
import com.devendra.springboot.repository.ScienceGradesDao;
import com.devendra.springboot.repository.StudentDao;
import com.devendra.springboot.service.StudentAndGradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Why @Transactional ?? --> We will make use of JPA Entity manager directly in this test
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Transactional
public class GradeBookControllerTest {

    private static MockHttpServletRequest req;

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    private StudentAndGradeService studentCreateServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CollegeStudent collegeStudent;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    private StudentAndGradeService studentAndGradeService;

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

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }

    @BeforeAll
    public static void setup() {
        req = new MockHttpServletRequest();
        req.setParameter("firstname", "Devendra");
        req.setParameter("lastname", "Thakur");
        req.setParameter("emailAddress", "devt@gmail.com");
    }


    @Test
    public void getStudentHttpRequest() throws Exception {

        collegeStudent.setFirstname("Kamya");
        collegeStudent.setLastname("Rao");
        collegeStudent.setEmailAddress("kamyarao@gmail.com");
        entityManager.persist(collegeStudent);

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void createStudentHttpRequest() throws Exception {

        collegeStudent.setFirstname("Kamya");
        collegeStudent.setLastname("Rao");
        collegeStudent.setEmailAddress("kamyarao@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(collegeStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        CollegeStudent verifyStudent = studentDao.findByEmailAddress("kamyarao@gmail.com");
        Assertions.assertNotNull(verifyStudent, "Student must exist");
    }


    @Test
    public void deleteStudentHttpRequest() throws Exception {

        Assertions.assertTrue(studentDao.findById(1).isPresent(), "Student must exist");

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));

        Assertions.assertFalse(studentDao.findById(1).isPresent(), "Student must not exist");
    }


    @Test
    public void deleteStudentHttpRequestErrorPage() throws Exception {

        Assertions.assertFalse(studentDao.findById(0).isPresent(), "Student must not exist");

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }


    @Test
    public void studentInformationHttpRequest() throws Exception {

        Optional<CollegeStudent> collegeStudent = studentDao.findById(1);
        Assertions.assertTrue(collegeStudent.isPresent(), "Student must exists");

        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Leo")))
                .andExpect(jsonPath("$.lastname", is("Messi")))
                .andExpect(jsonPath("$.emailAddress", is("leomessi@football.com")));
    }


    @Test
    public void studentInformationHttpRequestErrorPage() throws Exception {

        Optional<CollegeStudent> collegeStudent = studentDao.findById(0);
        Assertions.assertFalse(collegeStudent.isPresent(), "Student must not exist");

        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }


    @Test
    public void createGradeHttpRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .param("grade","100")
                .param("gradeType", "math")
                .param("studentId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Leo")))
                .andExpect(jsonPath("$.lastname", is("Messi")))
                .andExpect(jsonPath("$.emailAddress", is("leomessi@football.com")))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(2)));
    }


    @Test
    public void createGradeHttpRequestInvalidStudentId() throws Exception {

        Optional<CollegeStudent> collegeStudent = studentDao.findById(0);
        Assertions.assertFalse(collegeStudent.isPresent(), "Student must not exist");

        mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","100")
                        .param("gradeType", "math")
                        .param("studentId", "0"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }


    @Test
    public void createGradeHttpRequestInvalidGrade() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade","88")
                        .param("gradeType", "philosophy")
                        .param("studentId", "1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }


    @Test
    public void deleteGradeHttpRequest() throws Exception {

        Optional<MathGrade> mathGrade = mathGradeDao.findById(1);
        Assertions.assertTrue(mathGrade.isPresent(), "Grade must exist");

        mockMvc.perform(MockMvcRequestBuilders.delete(
                "/grades/{id}/{gradeType}", 1, "math"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Leo")))
                .andExpect(jsonPath("$.lastname", is("Messi")))
                .andExpect(jsonPath("$.emailAddress", is("leomessi@football.com")))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(0)));
    }


    @Test
    public void deleteGradeHttpRequestInvalidGradeId() throws Exception {

        Optional<MathGrade> mathGrade = mathGradeDao.findById(7);
        Assertions.assertFalse(mathGrade.isPresent(), "Grade must not exist");

        mockMvc.perform(MockMvcRequestBuilders.delete(
                "/grades/{id}/{gradeType}", 7, "math"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }


    @Test
    public void deleteGradeHttpRequestInvalidGradeType() throws Exception {

        Optional<MathGrade> mathGrade = mathGradeDao.findById(1);
        Assertions.assertTrue(mathGrade.isPresent(), "Grade must exist");

        mockMvc.perform(MockMvcRequestBuilders.delete(
                "/grades/{id}/{gradeType}", 1, "english"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));
    }
}
