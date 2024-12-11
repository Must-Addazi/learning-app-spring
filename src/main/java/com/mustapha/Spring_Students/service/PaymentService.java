package com.mustapha.Spring_Students.service;

import com.mustapha.Spring_Students.dtos.PaymentDTO;
import com.mustapha.Spring_Students.enums.PaymentStatus;
import com.mustapha.Spring_Students.exceptions.PaymentNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PaymentService {
    PaymentDTO savePayment(MultipartFile file, PaymentDTO paymentDTO) throws IOException;
    PaymentDTO updatePayment(Long paymentId, MultipartFile file, PaymentDTO paymentDTO) throws IOException;
    byte[] getPaymentFile( Long paymentId) throws IOException;
    void deletePayment(Long id) throws PaymentNotFoundException, IOException;
    PaymentDTO updatePaymentStatus(Long paymentID, PaymentStatus status) throws PaymentNotFoundException;
    PaymentDTO getPayment(Long id) throws PaymentNotFoundException;
    List<PaymentDTO> getPaymentList();
    List<PaymentDTO> getPaymentByCNE(String cne);

}
