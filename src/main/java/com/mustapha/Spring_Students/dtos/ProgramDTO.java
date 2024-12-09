package com.mustapha.Spring_Students.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ProgramDTO {
    private String id;
    private String name;
    private double price;
    private String poster;
    private ResponsibleProgramDTO responsibleProgramDTO;
    private List<ModuleDTO> moduleDTOList;
    private List<StudentDTO> studentDTOList;
}
