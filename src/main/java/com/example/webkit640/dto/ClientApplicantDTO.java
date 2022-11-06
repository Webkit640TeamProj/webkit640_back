package com.example.webkit640.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientApplicantDTO {
    private String name;
    private String major;
    private String schoolNumber;
    private String email;
    private List<FileDTO> files;

}
