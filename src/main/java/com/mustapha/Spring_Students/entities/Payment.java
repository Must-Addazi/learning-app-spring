package com.mustapha.Spring_Students.entities;

import com.mustapha.Spring_Students.enums.PayementStatus;
import com.mustapha.Spring_Students.enums.PayementType;
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
    private PayementType type ;
    @Enumerated(EnumType.STRING)
    private PayementStatus status=PayementStatus.CREATED;
    private String file;
    @ManyToOne
    private Student student;
}
