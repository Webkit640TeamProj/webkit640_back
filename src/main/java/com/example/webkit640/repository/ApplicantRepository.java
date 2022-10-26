package com.example.webkit640.repository;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    Member findByMember(int memberId);

    Applicant findBySchool(String school);

    Applicant findBySchoolNum(String schoolNum);

    Applicant findByMajor(String major);
    Applicant findByMemberId(int id);

}
