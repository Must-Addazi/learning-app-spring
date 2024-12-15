package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.NewStudentDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.service.PaymentService;
import com.mustapha.Spring_Students.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
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
    @GetMapping("/studentCNE/{code}")
   // @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public StudentDTO findStudentByCNE(@PathVariable String code){
        return studentService.findByCIN(code);
    }
    @GetMapping("/studentDTO/{programID}")
  //  @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<StudentDTO> findStudentByProgram(@PathVariable(name ="programID" ) String program) throws ProgramNotFoundException {
        return studentService.findByProgram(program);
    }
    @PostMapping(value = "/student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StudentDTO saveStudentDTO(@RequestParam("photoCIN") MultipartFile file, NewStudentDTO newStudentDTO) throws IOException, ProgramNotFoundException {
      return studentService.saveStudent(file,newStudentDTO);
    }
}
