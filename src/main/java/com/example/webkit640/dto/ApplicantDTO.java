package com.example.webkit640.dto;

import com.example.webkit640.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicantDTO {
    private String application;
    private boolean isApply;
    private boolean isSelect;
    private String major;
    private String school;
    private String schoolNumber;
    private List<FileDTO> files;
}
