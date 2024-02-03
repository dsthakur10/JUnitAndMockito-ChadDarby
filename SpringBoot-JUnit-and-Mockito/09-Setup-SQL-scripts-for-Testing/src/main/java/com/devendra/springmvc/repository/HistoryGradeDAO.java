package com.devendra.springmvc.repository;

import com.devendra.springmvc.models.HistoryGrade;
import org.springframework.data.repository.CrudRepository;

public interface HistoryGradeDAO extends CrudRepository<HistoryGrade, Integer> {

    public Iterable<HistoryGrade> findGradeByStudentId(int id);

    public void deleteByStudentId(int id);

}
