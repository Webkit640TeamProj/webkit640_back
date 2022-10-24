package com.example.webkit640.repository;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    Member findByMember_ID(int memberId);

    Applicant findBySchool(String school);

    Applicant findBySchoolNum(String schoolNum);

    Applicant findByMajor(String major);

}
