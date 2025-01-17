package com.mustapha.Spring_Students.entities;

import com.mustapha.Spring_Students.enums.PaymentType;
import com.mustapha.Spring_Students.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @Positive(message = "Le montant doit être supérieur à 0.")
    private double amount;
    @Enumerated(EnumType.STRING)
    private PaymentType type ;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status= com.mustapha.Spring_Students.enums.PaymentStatus.CREATED;
    @NotNull
    private String file;
    @ManyToOne
    private Student student;
}
