package com.example.webkit640.dto.response;

import com.example.webkit640.entity.Trainee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TraineeGetAllResponseDTO {
    private String name;
    private String schoolNumber;
    private String major;
    private String school;

    public static TraineeGetAllResponseDTO entityToDTO(Trainee trainee) {
        return TraineeGetAllResponseDTO.builder()
                .name(trainee.getApplicant().getName())
                .schoolNumber(trainee.getApplicant().getSchoolNum())
                .major(trainee.getApplicant().getMajor())
                .school(trainee.getApplicant().getSchool())
                .build();
    }
}
