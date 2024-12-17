package com.mustapha.Spring_Students.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ProgramDTO {
    private String id;
    private String name;
    private double price;
    private String timing;
    private ResponsibleProgramDTO responsibleProgramDTO;
}
