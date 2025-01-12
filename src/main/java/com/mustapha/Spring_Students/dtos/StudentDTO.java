package com.mustapha.Spring_Students.dtos;

import com.mustapha.Spring_Students.entities.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class StudentDTO {
    private String id ;
    private String CIN;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private Double NoteBac;
    private Double NoteDiploma;
    private double amountPaid;
    private String photoCIN;
    private String photo;
    private String bacFile;
    private String diplomaFile;
    private ProgramDTO programDTO;
}
