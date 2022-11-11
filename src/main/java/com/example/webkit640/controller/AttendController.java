package com.example.webkit640.controller;

import com.example.webkit640.dto.request.AttendAddRequestDTO;
import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Attend;
import com.example.webkit640.entity.Member;
import com.example.webkit640.entity.Trainee;
import com.example.webkit640.service.ApplyService;
import com.example.webkit640.service.AttendService;
import com.example.webkit640.service.MemberService;
import com.example.webkit640.service.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/attend")
@Slf4j
public class AttendController {
    private final MemberService memberService;
    private final AttendService attendService;
    private final ApplyService applicantService;
    private final TraineeService traineeService;

    @Autowired
    public AttendController(MemberService memberService, AttendService attendService, ApplyService applicantService, TraineeService traineeService) {
        this.memberService = memberService;
        this.attendService = attendService;
        this.applicantService = applicantService;
        this.traineeService = traineeService;
    }

    @PostMapping("/save-attend")
    public ResponseEntity<?> saveAttend(@AuthenticationPrincipal int id, @RequestBody AttendAddRequestDTO dto) {
        Member member = null;
        try {
            member = memberService.findByid(id);
            log.info("ENTER /attend/save-attend - Accessor : " + member.getEmail());
        } catch (NullPointerException ne) {
            log.error("USER NULL EXCEPTION /attend/save-attend");
            return ResponseEntity.badRequest().body("USER NULL EXCEPTION /attend/save-attend");
        }
        log.info(dto.getName()+dto.getMajor()+dto.getSchool()+dto.getSchoolNum());
        Applicant applicant = applicantService
                .getApplicantIdWithNameAndMajorAndSchoolAndSchoolName(dto.getName(), dto.getMajor(), dto.getSchool(), dto.getSchoolNum());
        log.info(applicant.getName());
        Trainee trainee = traineeService.getApplicantId(applicant.getId());
        Attend attend = AttendAddRequestDTO.dtoToEntity(dto);
        attend.setTrainee(trainee);
        attendService.saveAttend(attend);

        log.info("LEAVE /attend/save-attend - Accessor : " + member.getEmail());
        return ResponseEntity.ok().body("SUCCESS SAVE");
    }
}
