package com.mustapha.Spring_Students.security.entities;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String NewPassword;
}
