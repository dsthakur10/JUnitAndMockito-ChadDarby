package com.devendra.springmvc.controller;

import com.devendra.springmvc.models.Gradebook;
import com.devendra.springmvc.models.*;
import com.devendra.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

    @Autowired
    private Gradebook gradebook;

	@Autowired
	private StudentAndGradeService studentAndGradeService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStudents(Model m) {

		Iterable<CollegeStudent> collegeStudentIterable = studentAndGradeService.getGradebook();
		m.addAttribute("students", collegeStudentIterable);
        return "index";
    }


    @PostMapping("/")
    public String createStudent(@ModelAttribute("student") CollegeStudent student, Model m) {

        studentAndGradeService.createStudent(
                student.getFirstname(), student.getLastname(), student.getEmailAddress());

        Iterable<CollegeStudent> collegeStudentIterable = studentAndGradeService.getGradebook();
        m.addAttribute("students", collegeStudentIterable);
        return "index";
    }


    @GetMapping("/delete/student/{id}")
    public String deleteStudent(@PathVariable int id, Model m) {

        if (!studentAndGradeService.isStudentPresent(id)) {
            return "error";
        }
        studentAndGradeService.deleteStudent(id);

        Iterable<CollegeStudent> collegeStudentIterable = studentAndGradeService.getGradebook();
        m.addAttribute("students", collegeStudentIterable);
        return "index";
    }


    @GetMapping("/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model m) {
        if (!studentAndGradeService.isStudentPresent(id)) {
            return "error";
        }

        studentAndGradeService.configureStudentInformationModel(m, id);
        return "studentInformation";
    }


    @PostMapping("/grades")
    public String createGrade(@RequestParam("grade") double grade, @RequestParam("gradeType") String gradeType,
                              @RequestParam("studentId") int studentId, Model m) {

        if (!studentAndGradeService.isStudentPresent(studentId)) {
            return "error";
        }

        boolean success = studentAndGradeService.createGrade(grade, studentId, gradeType);

        if (!success) {
            return "error";
        }

        studentAndGradeService.configureStudentInformationModel(m, studentId);
        return "studentInformation";
    }


    @GetMapping("/grades/{id}/{gradeType}")
    public String deleteGrade(@PathVariable int id, @PathVariable String gradeType, Model m) {

        int studentId = studentAndGradeService.deleteGrade(id, gradeType);

        if (studentId == 0) {
            return "error";
        }

        studentAndGradeService.configureStudentInformationModel(m, studentId);
        return "studentInformation";
    }
}
