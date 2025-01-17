package com.mustapha.Spring_Students.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ResponsibleProgram {
    @Id
    private String id;
    private String name;
    private String phoneNumber;
    @NotBlank
    @Column(unique = true)
    private String email;
    @OneToOne(mappedBy = "responsibleProgram", cascade = CascadeType.ALL)
    private Program program;
}
