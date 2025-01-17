package com.mustapha.Spring_Students.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class Student {
    @Id
    private String id ;
    @Column(unique = true)
    @NotBlank(message = "CIN is mandatory")
    @Pattern(regexp = "^[A-Z0-9]{6,10}$", message = "Invalid CIN format")
    private String CIN;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "phone must be not empty")
    private String phone;
    @NotBlank
    private LocalDate birthDate;
    @Column(columnDefinition = "DOUBLE CHECK (NoteBac >= 10 AND NoteBac <= 20)", nullable = false)
    private Double NoteBac;
    @Column(columnDefinition = "DOUBLE CHECK (NoteDiploma >= 12 AND NoteDiploma <= 20)", nullable = false)
    private Double NoteDiploma;
    @Column(columnDefinition = "DOUBLE CHECK (amountPaid > 0)", nullable = false)
    private double amountPaid;
    private Boolean Convene;
    private Boolean selected;
    private String photoCIN;
    private String photo;
    private String bacFile;
    private String diplomaFile;
    @ManyToOne
    private Program program;
    @OneToMany(mappedBy = "student",fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Payment> paymentList;
}
