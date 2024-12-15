package com.mustapha.Spring_Students.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewStudentDTO {
    private String CIN;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String programID;
}
