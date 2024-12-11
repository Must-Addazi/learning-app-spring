package com.mustapha.Spring_Students.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ResponsibleProgram {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @OneToOne(mappedBy = "responsibleProgram", cascade = CascadeType.ALL)
    private Program program;
}
