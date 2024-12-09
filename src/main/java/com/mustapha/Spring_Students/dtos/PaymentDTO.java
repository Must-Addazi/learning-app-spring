package com.mustapha.Spring_Students.dtos;

import com.mustapha.Spring_Students.enums.PayementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentDTO {
    private LocalDate date;
    private double amount;
    private PayementType type ;
    private String studentCNE;
}
