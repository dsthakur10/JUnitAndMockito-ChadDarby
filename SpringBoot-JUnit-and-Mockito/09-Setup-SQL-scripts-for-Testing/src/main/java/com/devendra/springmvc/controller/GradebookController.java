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

    @GetMapping("/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model m) {
        return "studentInformation";
    }


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
}
