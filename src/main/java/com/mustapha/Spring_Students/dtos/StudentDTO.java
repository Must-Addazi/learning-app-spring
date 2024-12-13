package com.mustapha.Spring_Students.dtos;

import com.mustapha.Spring_Students.entities.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double amountPaid;
    private String photoCIN;
    private String photo;
    private ProgramDTO programDTO;
}
