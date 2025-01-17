package com.mustapha.Spring_Students.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewStudentDTO {
    private String CIN;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    @Max(value=20, message = "La note de bac doit être inférieur à 20.")
    @Min(value=10, message = "La note de bac doit être supérieur à 10 .")
    private Double NoteBac;
    @Max(value=20, message = "La note de diplome doit être inférieur à 20.")
    @Min(value=12, message = "La note de diplome doit être supérieur à 12 .")
    private Double NoteDiploma;
    private String programID;
}
