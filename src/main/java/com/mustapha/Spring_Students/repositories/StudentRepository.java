package com.mustapha.Spring_Students.repositories;

import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,String> {
    Student findByCIN(String cne);
    Student findByEmail(String email);
    @Query("SELECT s FROM Student s WHERE s.firstName LIKE %:name% OR s.lastName LIKE %:name%")
    List<Student> searchByName(@Param("name") String name);
    List<Student> findByProgram(Program program);
    List<Student> findByConveneTrue();
    List<Student> findByProgramAndConveneTrue(Program program);
}
