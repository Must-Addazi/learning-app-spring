package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.NewPaymentDTO;
import com.mustapha.Spring_Students.dtos.PaymentDTO;
import com.mustapha.Spring_Students.dtos.StudentDTO;
import com.mustapha.Spring_Students.entities.Payment;
import com.mustapha.Spring_Students.entities.Student;
import com.mustapha.Spring_Students.enums.PaymentStatus;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import com.mustapha.Spring_Students.mapping.Mapper;
import com.mustapha.Spring_Students.repositories.PaymentRepository;
import com.mustapha.Spring_Students.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;



@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private StudentRepository studentRepository;
    private PaymentRepository paymentRepository;
    private Mapper mapper;

    public PaymentDTO savePayment(MultipartFile file, NewPaymentDTO newPaymentDTO) throws IOException {
        Path path= Paths.get(System.getProperty("user.home"),"students-app-files","payments");
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        Student student=studentRepository.findByCNE(newPaymentDTO.getStudentCNE());
        PaymentDTO paymentDTO=mapper.fromNewpaymentDTO(newPaymentDTO);
        paymentDTO.setStudentDTO(mapper.fromStudent(student));
        paymentDTO.setStatus(PaymentStatus.CREATED);
        String FileID;
        if(student != null) {
             FileID = student.getFirstName() + student.getLastName() + UUID.randomUUID();
        }else {
            FileID= UUID.randomUUID().toString();
        }
        Path filePath= Paths.get(System.getProperty("user.home"),"students-app-files","payments",FileID+".pdf");
        if(file !=null)
        Files.copy(file.getInputStream(),filePath);
        paymentDTO.setFile(filePath.toUri().toString());
        Payment payment = mapper.fromPaymentDTO(paymentDTO);
        Payment savedPayment= paymentRepository.save(payment);
        student.setAmountPaid(student.getAmountPaid()+ payment.getAmount());
        studentRepository.save(student);
        return mapper.fromPayment(savedPayment);
    }
    public PaymentDTO updatePayment(Long paymentId, MultipartFile file, PaymentDTO paymentDTO) throws IOException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment with ID " + paymentId + " not found"));

        if (paymentDTO.getType() != null) {
            payment.setType(paymentDTO.getType());
        }
        if (paymentDTO.getDate() != null) {
            payment.setDate(paymentDTO.getDate());
        }
        Student student = studentRepository.findByCNE(paymentDTO.getStudentDTO().getCNE());
        if (paymentDTO.getStudentDTO().getCNE() != null) {
            if (student == null) {
                throw new IllegalArgumentException("Student with CNE " + paymentDTO.getStudentDTO().getCNE() + " not found");
            }
            payment.setStudent(student);
        }
        payment.setAmount(paymentDTO.getAmount());

        if (file != null && !file.isEmpty()) {
            if (payment.getFile() != null) {
                Path oldFilePath = Paths.get(URI.create(payment.getFile()));
                Files.deleteIfExists(oldFilePath);
            }

            Path path = Paths.get(System.getProperty("user.home"), "students-app-files", "payments");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            String newFileID = student.getFirstName()+student.getLastName()+student.getCNE();;
            Path newFilePath = Paths.get(System.getProperty("user.home"), "students-app-files", "payments", newFileID + ".pdf");
            Files.copy(file.getInputStream(), newFilePath);

            payment.setFile(newFilePath.toUri().toString());
        }

        Payment updatedPayment = paymentRepository.save(payment);

        return mapper.fromPayment(updatedPayment);
    }

    public byte[] getPaymentFile( Long paymentId) throws IOException, PaymentNotFoundException {
        Payment payment = mapper.fromPaymentDTO(getPayment(paymentId));
        String filePath= payment.getFile();
        return Files.readAllBytes(Path.of(URI.create(filePath)));
    }
    public void deletePayment(Long id) throws PaymentNotFoundException, IOException {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));

        String filePath = payment.getFile();
        if (filePath != null) {
            Path path = Paths.get(URI.create(filePath));
            if (Files.exists(path)) {
                Files.delete(path);
            }
        }
        paymentRepository.delete(payment);
    }

    @Override
    public PaymentDTO updatePaymentStatus(Long paymentID, PaymentStatus status) throws PaymentNotFoundException {
        PaymentDTO paymentDTO= getPayment(paymentID);
        Payment payment= mapper.fromPaymentDTO(paymentDTO);
        payment.setStatus(status);
        paymentRepository.save(payment);
        return mapper.fromPayment(payment);
    }

    @Override
    public PaymentDTO getPayment(Long id) throws PaymentNotFoundException {
        Payment payment=paymentRepository.findById(id).orElseThrow(()-> new PaymentNotFoundException("Payment not found"));
        return mapper.fromPayment(payment);
    }

    @Override
    public List<PaymentDTO> getPaymentList() {
        List<Payment> paymentList = paymentRepository.findAll();

        return paymentList.stream().map(payment -> mapper.fromPayment(payment)).toList();
    }


    @Override
    public List<PaymentDTO> getPaymentByCNE(String cne) {
        Student student=studentRepository.findByCNE(cne);
        List<Payment> paymentList=paymentRepository.findByStudent(student);
        return paymentList.stream().map(payment -> mapper.fromPayment(payment)).toList();
    }

}
