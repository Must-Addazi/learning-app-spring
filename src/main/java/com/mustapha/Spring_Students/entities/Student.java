package com.mustapha.Spring_Students.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class Student {
    @Id
    private String id ;
    @Column(unique = true)
    private String CNE;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private double amountPaid;
    private String photoCIN;
    @ManyToOne
    private Program program;
    @OneToMany(mappedBy = "student",fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Payment> paymentList;
}
