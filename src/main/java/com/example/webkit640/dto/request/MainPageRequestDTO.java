package com.example.webkit640.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPageRequestDTO {
    //모집 기간
    private String recruitmentDate;

    //모집 대상
    private String recruitmentTarget;

    //총 선발 인원
    private String totalRecruitment;

    //지원 자격
    private String eligibility;

    //서류 제출 기간
    private String documentSubmissionPeriod;

    //추가 모집 기간
    private String additionalRecruitmentPeriod;

    //합격 발표일
    private String passAnnouncementDate;

    //교육 시작일
    private String trainingStartDate;

    //총 교육생 수
    private String cumulativeStudents;

    //완료 기수
    private String completeCardinalNumber;

    //비전공자 수
    private String nonMajor;

    //담당자 연락처
    private String contact;

    //팝업 이미지 저장
    private String imagePath;
}
