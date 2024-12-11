package com.mustapha.Spring_Students.dtos;

import com.mustapha.Spring_Students.enums.PaymentType;
import com.mustapha.Spring_Students.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentDTO {
    private Long id;
    private LocalDate date;
    private double amount;
    @Enumerated(EnumType.STRING)
    private PaymentType type ;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String file;
    private StudentDTO studentDTO;
}
