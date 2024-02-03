package com.devendra.springmvc.service;

import com.devendra.springmvc.models.CollegeStudent;
import com.devendra.springmvc.models.HistoryGrade;
import com.devendra.springmvc.models.MathGrade;
import com.devendra.springmvc.models.ScienceGrade;
import com.devendra.springmvc.repository.HistoryGradeDAO;
import com.devendra.springmvc.repository.MathGradeDAO;
import com.devendra.springmvc.repository.ScienceGradeDAO;
import com.devendra.springmvc.repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    private final String MATH = "math";
    private final String HISTORY = "history";
    private final String SCIENCE = "science";

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private MathGradeDAO mathGradeDAO;

    @Autowired
    private ScienceGradeDAO scienceGradeDAO;

    @Autowired
    private HistoryGradeDAO historyGradeDAO;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    public void createStudent(String firstName, String lastName, String email) {
        CollegeStudent collegeStudent = new CollegeStudent(firstName, lastName, email);
        collegeStudent.setId(0);
        studentDAO.save(collegeStudent);
    }

    public boolean isStudentPresent(int id) {

        Optional<CollegeStudent> collegeStudent = studentDAO.findById(id);
        return collegeStudent.isPresent();
    }

    public void deleteStudent(int id) {

        if(isStudentPresent(id)) {
            studentDAO.deleteById(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> collegeStudentIterable = studentDAO.findAll();
        return collegeStudentIterable;
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {

        if (!isStudentPresent(studentId)) {
            return false;
        }

        if (grade >= 0 && grade <= 100) {
            switch (gradeType) {
                case MATH:
                    mathGrade.setId(0);
                    mathGrade.setStudentId(studentId);
                    mathGrade.setGrade(grade);
                    mathGradeDAO.save(mathGrade);
                    return true;

                case HISTORY:
                    historyGrade.setId(0);
                    historyGrade.setStudentId(studentId);
                    historyGrade.setGrade(grade);
                    historyGradeDAO.save(historyGrade);
                    return true;

                case SCIENCE:
                    scienceGrade.setId(0);
                    scienceGrade.setStudentId(studentId);
                    scienceGrade.setGrade(grade);
                    scienceGradeDAO.save(scienceGrade);
                    return true;

                default:
                    return false;
            }
        }

        return false;
    }
}
