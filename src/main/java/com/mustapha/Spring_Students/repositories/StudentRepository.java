package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,String> {
    Student findByCNE(String cne);
    @Query("SELECT s FROM Student s WHERE s.firstName LIKE %:name% OR s.lastName LIKE %:name%")
    List<Student> searchByName(@Param("name") String name);
    List<Student> findByProgram(Program program);

}
