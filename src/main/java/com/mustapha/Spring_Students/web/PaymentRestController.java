package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.NewPaymentDTO;
import com.mustapha.Spring_Students.entities.Payment;
import com.mustapha.Spring_Students.enums.PayementStatus;
import com.mustapha.Spring_Students.enums.PayementType;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.repositories.PayementRepository;
import com.mustapha.Spring_Students.repositories.StudentRepository;
import com.mustapha.Spring_Students.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Slf4j
@CrossOrigin("*")
@RestController
public class PaymentRestController {
    private PayementRepository payementRepository;
    private StudentRepository studentRepository;
    private PaymentService paymentService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentRestController.class);
    public PaymentRestController(PayementRepository payementRepository, StudentRepository studentRepository, PaymentService paymentService) {
        this.payementRepository = payementRepository;
        this.studentRepository = studentRepository;
        this.paymentService = paymentService;
    }

    @GetMapping("/payments")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<Payment> AllPayments(){
       return payementRepository.findAll();
    }
    @GetMapping("/payment/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public Payment findPayementBYid(@PathVariable Long id){
        return payementRepository.findById(id).get();
    }
    @GetMapping("/students")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<Student> AllStudents(){
        return studentRepository.findAll();
    }
    @GetMapping("/student/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public Student findStudentById( @PathVariable String id){
        return studentRepository.findById(id).get();
    }
    @GetMapping("/student/{code}/payment")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<Payment> findPaymentByStudentCode(@PathVariable String code){
        return payementRepository.findByStudentCode(code);
    }
    @PostMapping(value = "/payment",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public Payment savePayment(@RequestParam("file") MultipartFile file, NewPaymentDTO newPaymentDTO) throws IOException {
     return paymentService.savePayment(file,newPaymentDTO);
    }
    @GetMapping(value = "/paymentFile/{paymentId}",produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public byte[] getPaymentFile(@PathVariable Long paymentId) throws IOException {
     return paymentService.getPaymentFile(paymentId);
    }
    @PutMapping("/payments/updateStatus/{paymentID}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public Payment updatePaymentStatus(@PathVariable Long paymentID, @RequestParam PayementStatus status){
       return paymentService.updatePaymentStatus(paymentID,status);
    }
    @GetMapping("/StudentCode/{code}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public Student findStudentByCode(@PathVariable String code){
        return studentRepository.findByCode(code);
    }
    @GetMapping("/Student/{programID}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<Student> findStudentByProgramID( @PathVariable String programID){
        return studentRepository.findByProgramID(programID);
    }
    @GetMapping("/Payment/Status/{status}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<Payment> findPaymentByStatus(@PathVariable PayementStatus status){
        return payementRepository.findByStatus(status);
    }
    @GetMapping("/Payment/Type/{type}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<Payment> findPaymentByType(@PathVariable PayementType type){
        return payementRepository.findByType(type);
    }
}
