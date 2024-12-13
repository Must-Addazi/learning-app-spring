package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getStudentList();
    StudentDTO getStudent(String id) throws StudentNotFoundException;
    StudentDTO saveStudent(StudentDTO studentDTO);
    void deleteStudent(String id) throws StudentNotFoundException;
    StudentDTO updateStudent(String id,StudentDTO studentDTO);
    List<StudentDTO> searchStudentByName(String name);

    StudentDTO findByCNE(String code);

    List<StudentDTO> findByProgram(String program) throws ProgramNotFoundException;
}
