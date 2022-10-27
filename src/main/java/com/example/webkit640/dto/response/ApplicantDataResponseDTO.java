package com.example.webkit640.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicantDataResponseDTO {
    private String name;
    private String school;
    private String schoolNumber;
    private String major;
    private String application;
}
