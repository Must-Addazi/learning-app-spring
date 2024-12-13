package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.PaymentDTO;
import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.entities.Program;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class StudentServiceImpl implements StudentService{
    private StudentRepository studentRepository;
    private ProgramService programService;
    private Mapper mapper;
    @Override
    public List<StudentDTO> getStudentList() {
        List<Student> studentList=studentRepository.findAll();
        return studentList.stream().map(student ->
                mapper.fromStudent(student)).toList();
    }

    @Override
    public StudentDTO getStudent(String id) throws StudentNotFoundException {
        Student student = studentRepository.findById(id).orElseThrow(() ->new StudentNotFoundException("Student not found"));
        return mapper.fromStudent(student);
    }

    @Override
    public StudentDTO saveStudent(StudentDTO studentDTO) {
        Student student = mapper.fromStudentDTO(studentDTO);
        Student savedstudent= studentRepository.save(student);
        return mapper.fromStudent(savedstudent);
    }

    @Override
    public void deleteStudent(String id) throws StudentNotFoundException {
        StudentDTO studentDTO= getStudent(id);
        Student student= mapper.fromStudentDTO(studentDTO);
    studentRepository.delete(student);
    }

    @Override
    public StudentDTO updateStudent(String id,StudentDTO studentDTO) {
        Student student= mapper.fromStudentDTO(studentDTO);
        student.setId(id);
        Student upStudent= studentRepository.save(student);
        return mapper.fromStudent(upStudent);
    }

    @Override
    public List<StudentDTO> searchStudentByName(String name) {
        List<Student> studentList = studentRepository.searchByName(name);
        return studentList.stream().map(student -> mapper.fromStudent(student)).toList();
    }


    @Override
    public StudentDTO findByCNE(String code) {
        return mapper.fromStudent(studentRepository.findByCNE(code));
    }

    @Override
    public List<StudentDTO> findByProgram(String programId) throws ProgramNotFoundException {
        Program program = mapper.fromProgramDTO(programService.getProgram(programId));
        List<Student> studentList= studentRepository.findByProgram(program);
        return studentList.stream().map(student -> mapper.fromStudent(student)).toList();
    }
}
