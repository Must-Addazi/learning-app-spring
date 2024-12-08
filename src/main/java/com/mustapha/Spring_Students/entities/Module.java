package com.mustapha.Spring_Students.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Module {
    @Id
   private String id;
   private String name;
   private String teacherName;
   @ManyToOne
   @JoinColumn(name = "program_id")
   private Program program;
}
