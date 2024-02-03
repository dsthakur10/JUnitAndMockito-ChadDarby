package com.devendra.springmvc.service;

import com.devendra.springmvc.models.*;
import com.devendra.springmvc.repository.HistoryGradeDAO;
import com.devendra.springmvc.repository.MathGradeDAO;
import com.devendra.springmvc.repository.ScienceGradeDAO;
import com.devendra.springmvc.repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private StudentGrades studentGrades;


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

        if (isStudentPresent(id)) {
            studentDAO.deleteById(id);
            mathGradeDAO.deleteByStudentId(id);
            scienceGradeDAO.deleteByStudentId(id);
            historyGradeDAO.deleteByStudentId(id);
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

    // returns the Student-ID whose grade is deleted with given grade-ID
    public int deleteGrade(int gradeId, String gradeType) {
        int studentId = 0;

        switch (gradeType) {
            case MATH:
                Optional<MathGrade> mathGrade = mathGradeDAO.findById(gradeId);
                if (!mathGrade.isPresent()) {
                    return studentId;
                }
                studentId = mathGrade.get().getStudentId();
                mathGradeDAO.deleteById(gradeId);
                return studentId;

            case HISTORY:
                Optional<HistoryGrade> historyGrade = historyGradeDAO.findById(gradeId);
                if (!historyGrade.isPresent()) {
                    return studentId;
                }
                studentId = historyGrade.get().getStudentId();
                historyGradeDAO.deleteById(gradeId);
                return studentId;

            case SCIENCE:
                Optional<ScienceGrade> scienceGrade = scienceGradeDAO.findById(gradeId);
                if (!scienceGrade.isPresent()) {
                    return studentId;
                }
                studentId = scienceGrade.get().getStudentId();
                scienceGradeDAO.deleteById(gradeId);
                return studentId;

            default:
                return 0;
        }
    }

    public GradebookCollegeStudent studentInformation(int id) {
        Optional<CollegeStudent> collegeStudent = studentDAO.findById(id);

        if (!collegeStudent.isPresent()) {
            return null;
        }

        Iterable<MathGrade> mathGrades = mathGradeDAO.findGradeByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDAO.findGradeByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradeDAO.findGradeByStudentId(id);

        List<Grade> mathGradeList = new ArrayList<>();
        mathGrades.forEach(mathGradeList::add);

        List<Grade> scienceGradeList = new ArrayList<>();
        scienceGrades.forEach(scienceGradeList::add);

        List<Grade> historyGradeList = new ArrayList<>();
        historyGrades.forEach(historyGradeList::add);

        studentGrades.setMathGradeResults(mathGradeList);
        studentGrades.setScienceGradeResults(scienceGradeList);
        studentGrades.setHistoryGradeResults(historyGradeList);

        GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(
                collegeStudent.get().getId(), collegeStudent.get().getFirstname(), collegeStudent.get().getLastname(),
                collegeStudent.get().getEmailAddress(), studentGrades);

        return gradebookCollegeStudent;
    }


    public void configureStudentInformationModel(Model m, int studentId) {

        GradebookCollegeStudent gradebookCollegeStudent = studentInformation(studentId);

        m.addAttribute("student",gradebookCollegeStudent);
        if (gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() > 0) {
            m.addAttribute("mathAverage",
                    gradebookCollegeStudent.getStudentGrades().findGradePointAverage(
                            gradebookCollegeStudent.getStudentGrades().getMathGradeResults()
                    ));
        } else {
            m.addAttribute("mathAverage", "N/A");
        }

        m.addAttribute("student",gradebookCollegeStudent);
        if (gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() > 0) {
            m.addAttribute("scienceAverage",
                    gradebookCollegeStudent.getStudentGrades().findGradePointAverage(
                            gradebookCollegeStudent.getStudentGrades().getScienceGradeResults()
                    ));
        } else {
            m.addAttribute("scienceAverage", "N/A");
        }

        m.addAttribute("student",gradebookCollegeStudent);
        if (gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() > 0) {
            m.addAttribute("historyAverage",
                    gradebookCollegeStudent.getStudentGrades().findGradePointAverage(
                            gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults()
                    ));
        } else {
            m.addAttribute("historyAverage", "N/A");
        }
    }
}
