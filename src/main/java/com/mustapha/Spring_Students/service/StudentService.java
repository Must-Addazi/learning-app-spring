package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getStudentList();
    StudentDTO getStudent(String id) throws StudentNotFoundException;
    StudentDTO saveStudent(StudentDTO studentDTO);
    void deleteStudent(String id) throws StudentNotFoundException;
    StudentDTO updateStudent(String id,StudentDTO studentDTO);
    List<StudentDTO> searchStudentByName(String name);
    StudentDTO searchStudentByCNE(String CNE);
}
