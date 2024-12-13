package com.mustapha.Spring_Students.dtos;

import com.mustapha.Spring_Students.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class NewPaymentDTO {
   private String studentCNE;
   private LocalDate date;
   private Double amount;
   private PaymentType type;
}
