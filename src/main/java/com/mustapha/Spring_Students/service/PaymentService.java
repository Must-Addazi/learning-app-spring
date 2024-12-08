package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.NewPaymentDTO;
import com.mustapha.Spring_Students.entities.Payment;
import com.mustapha.Spring_Students.enums.PayementStatus;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.repositories.PayementRepository;
import com.mustapha.Spring_Students.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {
    private PayementRepository payementRepository;
    private StudentRepository studentRepository;

    public PaymentService(PayementRepository payementRepository, StudentRepository studentRepository) {
        this.payementRepository = payementRepository;
        this.studentRepository = studentRepository;
    }
    public Payment savePayment(MultipartFile file, NewPaymentDTO newPaymentDTO) throws IOException {
        Path path= Paths.get(System.getProperty("user.home"),"students-app-files","payements");
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        String FileID= UUID.randomUUID().toString();
        Path FilePath= Paths.get(System.getProperty("user.home"),"students-app-files","payements",FileID+".pdf");
        Files.copy(file.getInputStream(),FilePath);
        Student student=studentRepository.findByCode(newPaymentDTO.getStudentCode());
        Payment payment = Payment.builder()
                .type(newPaymentDTO.getType())
                .amount(newPaymentDTO.getAmount())
                .date(newPaymentDTO.getDate())
                .student(student)
                .status(PayementStatus.CREATED)
                .file(FilePath.toUri().toString())
                .build();
        return payementRepository.save(payment);
    }
    public Payment updatePaymentStatus( Long paymentID, PayementStatus status){
        Payment payment = payementRepository.findById(paymentID).get();
        payment.setStatus(status);
        return payementRepository.save(payment);
    }
    public byte[] getPaymentFile( Long paymentId) throws IOException {
        Payment payment = payementRepository.findById(paymentId).get();
        String filePath= payment.getFile();
        return Files.readAllBytes(Path.of(URI.create(filePath)));
    }
}
