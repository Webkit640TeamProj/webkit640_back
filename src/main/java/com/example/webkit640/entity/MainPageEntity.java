package com.example.webkit640.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "t_mainpage")
@Data
public class MainPageEntity extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

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


}
