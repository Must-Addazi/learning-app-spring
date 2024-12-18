package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.NewStudentDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudentService {
    List<StudentDTO> getStudentList();
    StudentDTO getStudent(String id) throws StudentNotFoundException;
    StudentDTO saveStudent(MultipartFile file,MultipartFile profile , NewStudentDTO newStudentDTO) throws IOException, ProgramNotFoundException;
    void deleteStudent(String id) throws StudentNotFoundException;
    StudentDTO updateStudent(String id,StudentDTO studentDTO);
    List<StudentDTO> searchStudentByName(String name);

    StudentDTO findByCIN(String code);

    List<StudentDTO> findByProgram(String program) throws ProgramNotFoundException;
}
