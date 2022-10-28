package com.example.webkit640.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDTO {
    private int id;
    private String email;
    private String memberType;
    private String memberBelong;
    private String token;
    private String password;
    private boolean is_admin;
}
