package com.example.webkit640.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyDTO {
    private String name;
    private String major;
    private String school;
    private String schoolNumber;
    private String application;
    private String schoolYear;
}
