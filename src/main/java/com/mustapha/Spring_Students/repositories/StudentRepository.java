package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,String> {
    Student findByCode(String code);
    List<Student> findByProgramID(String programID);
}
