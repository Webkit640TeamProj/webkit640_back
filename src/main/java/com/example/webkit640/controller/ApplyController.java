package com.example.webkit640.controller;

import com.example.webkit640.dto.ApplyDTO;
import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.FileEntity;
import com.example.webkit640.entity.Member;
import com.example.webkit640.service.ApplyService;
import com.example.webkit640.service.FileService;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/apply")
@Slf4j
public class ApplyController {
    private final FileService fileService;
    private final ApplyService applyService;
    private final MemberService memberService;
    @Autowired
    public ApplyController(FileService fileService, ApplyService applyService, MemberService memberService) {
        this.fileService = fileService;
        this.applyService = applyService;
        this.memberService = memberService;
    }

    @PostMapping("/applies")
    public ResponseEntity<?> uploadTest(@RequestPart MultipartFile file, @RequestPart ApplyDTO applyDTO, @AuthenticationPrincipal int id) throws IOException {
        Member member = memberService.findByid(id);
        log.info(member.toString());

        Applicant applicant = Applicant.builder()
                .application(applyDTO.getApplication())
                .isApply(false)
                .member(member)
                .isSelect(false)
                .major(applyDTO.getMajor())
                .school(applyDTO.getSchool())
                .schoolNum(applyDTO.getSchoolNumber())
                .build();
        Applicant resultApplicant = applyService.saveApplicant(applicant);


        FileEntity fileEntity = fileService.saveFile(file,resultApplicant);

        Applicant modifyApplicant = Applicant.builder()
                .application(resultApplicant.getApplication())
                .id(resultApplicant.getId())
                .schoolNum(resultApplicant.getSchoolNum())
                .school(resultApplicant.getSchool())
                .isApply(resultApplicant.isApply())
                .member(resultApplicant.getMember())
                .major(resultApplicant.getMajor())
                .isSelect(resultApplicant.isSelect())
                .build();
        Applicant saveApplicant = applyService.saveApplicant(modifyApplicant);
        return ResponseEntity.ok().body(fileEntity);
    }
}
