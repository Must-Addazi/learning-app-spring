package com.mustapha.Spring_Students.entities;

import com.mustapha.Spring_Students.enums.PaymentType;
import com.mustapha.Spring_Students.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private double amount;
    @Enumerated(EnumType.STRING)
    private PaymentType type ;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status= com.mustapha.Spring_Students.enums.PaymentStatus.CREATED;
    private String file;
    @ManyToOne
    private Student student;
}
