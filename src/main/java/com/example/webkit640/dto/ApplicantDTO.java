package com.example.webkit640.dto;

import com.example.webkit640.entity.Applicant;
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
    private String date;
    private String name;
    private String application;
    private boolean isApply;
    private boolean isSelect;
    private String major;
    private String school;
    private String schoolNumber;
    private List<FileDTO> files;
    private String email;
    private String schoolYear;
    private boolean isAdminSelect;
    public static ApplicantDTO entityToDTO(Applicant applicant) {
        return ApplicantDTO.builder()
                .school(applicant.getSchool())
                .name(applicant.getName())
                .application(applicant.getApplication())
                .major(applicant.getMajor())
                .schoolNumber(applicant.getSchoolNum())
                .isSelect(applicant.isSelect())
                .isApply(applicant.isApply())
                .build();
    }
}
