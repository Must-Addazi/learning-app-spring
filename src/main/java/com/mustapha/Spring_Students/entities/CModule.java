package com.mustapha.Spring_Students.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CModule {
    @Id
   private String id;
   private String name;
   private String teacherName;
   @ManyToOne
   private Program program;
}
