package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.NewStudentDTO;
import com.mustapha.Spring_Students.dtos.PaymentDTO;
import com.mustapha.Spring_Students.dtos.ProgramDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.enums.PaymentStatus;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.exceptions.ProgramNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.security.entities.AppUser;
import com.mustapha.Spring_Students.security.service.AccountService;
import com.mustapha.Spring_Students.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class StudentRestController {
    private StudentService studentService;
    private AccountService accountService;
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
    @DeleteMapping("/deleteStudent/{id}")
    public Boolean deleteStudent(@PathVariable String id) throws StudentNotFoundException, IOException, PaymentNotFoundException {
        return studentService.deleteStudent(id);
    }
    @GetMapping("/posterFile/{studentId}/{file}")
    public ResponseEntity<byte[]> getFile(@PathVariable String studentId, @PathVariable String file) {
        try {
            byte[] fileBytes = studentService.getFile(studentId,file);
            StudentDTO studentDTO= studentService.getStudent(studentId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename(studentDTO.getCIN()+file+ ".pdf").build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/updateStudent/{studentId}")
    //  @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public StudentDTO updateStudent(@PathVariable String studentId,
                                    @RequestParam String CIN,
                                    @RequestParam(required = false) Double NoteBac,
                                    @RequestParam(required = false) Double NoteDiploma,
                                    @RequestBody NewStudentDTO studentDTO) throws StudentNotFoundException {
       studentDTO.setCIN(CIN);
       studentDTO.setNoteBac(NoteBac);
       studentDTO.setNoteDiploma(NoteDiploma);
        return studentService.updateStudent(studentId,studentDTO);
    }
    @PutMapping("/updateStudentFile/{studentId}/{fileType}")
    public StudentDTO updateStudentFile(
            @PathVariable String studentId,
            @PathVariable String fileType,
            @RequestParam("file") MultipartFile multipartFile
    ) throws StudentNotFoundException, IOException {
       return studentService.updateFile(studentId, multipartFile, fileType);
    }
    @PutMapping("/updateStudentPassword/{studentEmail}")
    public AppUser updateStudentPassword(
            @PathVariable String studentEmail,
            @RequestBody Map<String, String> payload) {
        String password = payload.get("password");
        String confirmPassword = payload.get("confirmPassword");
        return accountService.upadatePassword(studentEmail,password,confirmPassword);
    }
    @PutMapping("/conveneStudent/{studentId}")
    public StudentDTO conveneStudent(@PathVariable(value = "studentId") String id){
        return studentService.conveneStudent(id);
    }

}
