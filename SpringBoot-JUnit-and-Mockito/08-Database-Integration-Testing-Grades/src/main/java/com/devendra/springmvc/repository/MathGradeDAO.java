package com.devendra.springmvc.repository;

import com.devendra.springmvc.models.MathGrade;
import org.springframework.data.repository.CrudRepository;

public interface MathGradeDAO extends CrudRepository<MathGrade, Integer> {

    public Iterable<MathGrade> findGradeByStudentId(int id);

    public void deleteByStudentId(int id);
}
