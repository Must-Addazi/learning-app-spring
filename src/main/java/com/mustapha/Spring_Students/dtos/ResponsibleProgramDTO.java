package com.mustapha.Spring_Students.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ResponsibleProgramDTO {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
}
