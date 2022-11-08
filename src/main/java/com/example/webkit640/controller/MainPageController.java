package com.example.webkit640.controller;


import com.example.webkit640.dto.request.MainPageRequestDTO;
import com.example.webkit640.entity.MainPageEntity;
import com.example.webkit640.service.MainPageService;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequestMapping("/main")
@Slf4j
@RestController
public class MainPageController {
    private final MainPageService mainPageService;
    private final MemberService memberService;

    @Autowired
    public MainPageController(MainPageService mainPageService, MemberService memberService) {
        this.mainPageService = mainPageService;
        this.memberService = memberService;
    }

    @GetMapping("/data")
    public ResponseEntity<?> readData(@AuthenticationPrincipal int id) {
        try {
            log.info("ENTER /main/data - Accessor : "+memberService.findByid(id).getEmail());
        } catch (NullPointerException ne) {
            return ResponseEntity.badRequest().body("NO USER");
        }
        MainPageEntity entity = mainPageService.getById();
        log.info("LEAVE /main/data - Accessor : "+memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body(entity);
    }

    @PutMapping("/admin-modify")
    public ResponseEntity<?> adminModify(@AuthenticationPrincipal int id, @RequestBody MainPageRequestDTO dto) {
        try {
            log.info("ENTER /main/admin-modify - Accessor : "+memberService.findByid(id).getEmail());
        } catch (NullPointerException ne) {
            return ResponseEntity.badRequest().body("NO USER");
        }

        try {
            MainPageEntity entity = mainPageService.getById();
            entity.setCumulativeStudents(dto.getCumulativeStudents());
            entity.setAdditionalRecruitmentPeriod(dto.getAdditionalRecruitmentPeriod());
            entity.setEligibility(dto.getEligibility());
            entity.setCompleteCardinalNumber(dto.getCompleteCardinalNumber());

            entity.setDocumentSubmissionPeriod(dto.getDocumentSubmissionPeriod());
            entity.setNonMajor(dto.getNonMajor());
            entity.setRecruitmentDate(dto.getRecruitmentDate());
            entity.setRecruitmentTarget(dto.getRecruitmentTarget());

            entity.setPassAnnouncementDate(dto.getPassAnnouncementDate());
            entity.setTotalRecruitment(dto.getTotalRecruitment());
            entity.setTrainingStartDate(dto.getTrainingStartDate());

            MainPageEntity saveData = mainPageService.saveData(entity);
            log.info("LEAVE /main/admin-modify - Accessor : "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(saveData);
        } catch (NullPointerException ne) {
            MainPageEntity entity = MainPageEntity.builder()
                    .cumulativeStudents(dto.getCumulativeStudents())
                    .documentSubmissionPeriod(dto.getDocumentSubmissionPeriod())
                    .completeCardinalNumber(dto.getCompleteCardinalNumber())
                    .recruitmentTarget(dto.getRecruitmentTarget())
                    .totalRecruitment(dto.getTotalRecruitment())
                    .trainingStartDate(dto.getTrainingStartDate())
                    .additionalRecruitmentPeriod(dto.getAdditionalRecruitmentPeriod())
                    .eligibility(dto.getEligibility())
                    .nonMajor(dto.getNonMajor())
                    .passAnnouncementDate(dto.getPassAnnouncementDate())
                    .recruitmentDate(dto.getRecruitmentDate())
                    .build();
            MainPageEntity saveData = mainPageService.saveData(entity);
            log.info("LEAVE /main/admin-modify - Accessor : "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(saveData);
        }
    }
}
