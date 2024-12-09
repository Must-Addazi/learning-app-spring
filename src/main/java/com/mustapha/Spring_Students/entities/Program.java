package com.mustapha.Spring_Students.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Program {
    @Id
    private String id;
    private String name;
    private double price;
    private String poster;
    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ResponsibleProgram responsibleProgram;
    @OneToMany(mappedBy = "programDTO",fetch = FetchType.LAZY)
    private List<CModule> CModuleList;
    @OneToMany(mappedBy = "programDTO",fetch = FetchType.LAZY)
    private List<Student> studentList;
}
