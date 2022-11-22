package com.example.webkit640.dto.response;

import com.example.webkit640.entity.Applicant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicantMemberApplicantResponseDTO {
    private String name;
    private String major;
    private String schoolNumber;
    private boolean isAdminApply;
    private boolean isSelect;
    private String date;

    public static ApplicantMemberApplicantResponseDTO entityToDTO(Applicant applicant) {
        return ApplicantMemberApplicantResponseDTO.builder()
                .name(applicant.getName())
                .major(applicant.getMajor())
                .schoolNumber(applicant.getSchoolNum())
                .date(applicant.getUpdateDate())
                .isAdminApply(applicant.isAdminApply())
                .isSelect(applicant.isSelect())
                .build();
    }

}
