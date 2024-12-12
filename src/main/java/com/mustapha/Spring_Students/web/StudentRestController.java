package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.service.PaymentService;
import com.mustapha.Spring_Students.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class StudentRestController {
    private PaymentService paymentService;
    private StudentService studentService;
    @GetMapping("/students")
  //  @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<StudentDTO> AllStudents(){
        return studentService.getStudentList();
    }
    @GetMapping("/student/{id}")
   // @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public StudentDTO findStudentById( @PathVariable String id) throws StudentNotFoundException {
        return studentService.getStudent(id);
    }
    @GetMapping("/StudentCNE/{code}")
   // @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public StudentDTO findStudentByCNE(@PathVariable String code){
        return studentService.findByCNE(code);
    }
    @GetMapping("/StudentDTO/{programID}")
  //  @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<StudentDTO> findStudentByProgram(@PathVariable String program){
        return studentService.findByProgram(program);
    }
}
