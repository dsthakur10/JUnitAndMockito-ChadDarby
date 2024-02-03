package com.devendra.springmvc.service;

import com.devendra.springmvc.models.CollegeStudent;
import com.devendra.springmvc.repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDAO studentDAO;

    public boolean isStudentPresent(int id) {

        Optional<CollegeStudent> collegeStudent = studentDAO.findById(id);
        return collegeStudent.isPresent();
    }

    public void createStudent(String firstName, String lastName, String email) {
        CollegeStudent collegeStudent = new CollegeStudent(firstName, lastName, email);
        collegeStudent.setId(0);
        studentDAO.save(collegeStudent);
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
}
