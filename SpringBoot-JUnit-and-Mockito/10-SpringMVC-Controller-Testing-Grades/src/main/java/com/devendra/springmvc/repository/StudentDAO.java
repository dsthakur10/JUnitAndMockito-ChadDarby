package com.devendra.springmvc.repository;

import com.devendra.springmvc.models.CollegeStudent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDAO extends CrudRepository<CollegeStudent, Integer> {

    // Add custom methods that are not available in CrudRepo.
    // Behind the scenes, Spring Data JPA will automatically perform the query on the DB for us

    public CollegeStudent findByEmailAddress(String emailAddress);
}
