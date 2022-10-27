package com.example.webkit640.service;

import com.example.webkit640.entity.Applicant;
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
        if (type.equals("name")) {
            return applicantRepository.findByName(keyword);
        } else if (type.equals("school")) {
            return applicantRepository.findBySchool(keyword);
        } else {
            return applicantRepository.findByMajor(keyword);
        }
    }
//    public Applicant findByApplicantOne(int memberId) {
//        return applicantRepository.findByMember_Id(memberId);
//    }
}
