package com.mustapha.Spring_Students.web;

import com.mustapha.Spring_Students.dtos.NewPaymentDTO;
import com.mustapha.Spring_Students.dtos.PaymentDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.enums.PaymentStatus;
import com.mustapha.Spring_Students.enums.PaymentType;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.exceptions.StudentNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.service.PaymentService;
import com.mustapha.Spring_Students.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@Slf4j
@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class PaymentRestController {
    private PaymentService paymentService;
    private StudentService studentService;

    @GetMapping("/payments")
  //  @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<PaymentDTO> AllPayments(){
       return paymentService.getPaymentList();
    }
    @GetMapping("/payment/{id}")
  //  @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public PaymentDTO findPayementBYid(@PathVariable Long id) throws PaymentNotFoundException {
        return paymentService.getPayment(id);
    }
    @GetMapping("/student/{code}/payment")
   // @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<PaymentDTO> findPaymentByStudentCode(@PathVariable String code){
        return paymentService.getPaymentByCNE(code);
    }
    @PostMapping(value = "/payment",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   // @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public PaymentDTO savePayment(@RequestParam("file") MultipartFile file, NewPaymentDTO newPaymentDTO
                                   ) throws IOException {
        return paymentService.savePayment(file,newPaymentDTO);
    }

    @GetMapping(value = "/paymentFile/{paymentId}",produces = MediaType.APPLICATION_PDF_VALUE)
 //   @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public byte[] getPaymentFile(@PathVariable Long paymentId) throws IOException,PaymentNotFoundException {
     return paymentService.getPaymentFile(paymentId);
    }
    @PutMapping("/payments/updateStatus/{paymentID}")
  //  @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public PaymentDTO updatePaymentStatus(@PathVariable Long paymentID, @RequestParam PaymentStatus status) throws PaymentNotFoundException {
       return paymentService.updatePaymentStatus(paymentID,status);
    }


}
