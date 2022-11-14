package com.example.webkit640.service;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Member;
import com.example.webkit640.repository.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ApplyService {
    @Autowired
    private ApplicantRepository applicantRepository;

    public Applicant saveApplicant(Applicant applicant) {
        return applicantRepository.save(applicant);
    }
    public List<Applicant> findAllApplicant() {
        return applicantRepository.findAll();
    }
    public Applicant getByMemberId(int id) {
        return applicantRepository.findByMemberId(id);
    }

    public List<Applicant> getSearchApplicant(String type, String keyword) {

        //QueryDSL
        if (type.equals("name")) {
            return applicantRepository.findByName(keyword);
        } else if (type.equals("school")) {
            return applicantRepository.findBySchool(keyword);
        } else {
            return applicantRepository.findByMajor(keyword);
        }
    }

    public Boolean checkApplicant(Member member) {
        return applicantRepository.existsByMember_Id(member.getId());
    }
//    public Applicant findByApplicantOne(int memberId) {
//        return applicantRepository.findByMember_Id(memberId);
//    }
    public List<Applicant> getDateApplicant(String yearMonth) {
        return applicantRepository.findAllByCreateDateContaining(yearMonth);
    }

    public Applicant getApplicantIdWithNameAndMajorAndSchoolAndSchoolName(String name, String major, String school, String schoolNum) {
        return applicantRepository.findByNameAndMajorAndSchoolAndSchoolNum(name, major, school, schoolNum);
    }
}
