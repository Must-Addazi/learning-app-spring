package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.NewStudentDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class StudentRestController {
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
    @GetMapping("/studentEmail/{email}")
    // @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public StudentDTO findStudentByEmail(@PathVariable String email){
        return studentService.findByEmail(email);
    }
    @GetMapping("/studentDTO/{programID}")
  //  @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<StudentDTO> findStudentByProgram(@PathVariable(name ="programID" ) String program) throws ProgramNotFoundException {
        return studentService.findByProgram(program);
    }
    @PostMapping(value = "/saveStudent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StudentDTO saveStudentDTO(@RequestParam("photoCIN") MultipartFile CinFile,
                                     @RequestParam("bacFile") MultipartFile bacFile,
                                     @RequestParam("diplomaFile") MultipartFile diplomaFile,
                                     @RequestParam(value = "profile", required = false) MultipartFile profile, NewStudentDTO newStudentDTO) throws IOException, ProgramNotFoundException {
      return studentService.saveStudent(CinFile,bacFile,diplomaFile,profile,newStudentDTO);
    }
}
