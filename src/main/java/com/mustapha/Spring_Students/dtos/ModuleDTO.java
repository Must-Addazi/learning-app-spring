package com.mustapha.Spring_Students.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ModuleDTO {
   private String id;
   private String name;
   private String teacherName;
   private ProgramDTO programDTO;
}
