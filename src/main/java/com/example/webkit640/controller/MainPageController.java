package com.example.webkit640.controller;


import com.example.webkit640.dto.request.MainPageRequestDTO;
import com.example.webkit640.dto.response.MainPageReviewResponseDTO;
import com.example.webkit640.entity.Board;
import com.example.webkit640.entity.MainPageEntity;
import com.example.webkit640.service.BoardService;
import com.example.webkit640.service.MainPageService;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/main")
@Slf4j
@RestController
public class MainPageController {
    private final MainPageService mainPageService;
    private final MemberService memberService;
    private final BoardService boardService;

    @Autowired
    public MainPageController(MainPageService mainPageService, MemberService memberService, BoardService boardService) {
        this.mainPageService = mainPageService;
        this.memberService = memberService;
        this.boardService = boardService;
    }

    @GetMapping("/review")
    public ResponseEntity<?> readReview() {
        try {
            log.info("ENTER /main/review");
        } catch (NullPointerException ne) {
            log.error("USER NULL /main/review");
            ResponseEntity.badRequest().body("USER NULL /main/review");
        }
        List<Board> reviewBoardAndMainPage = boardService.getReviewBoardAndMainPage();
        Collections.reverse(reviewBoardAndMainPage);
        List<Board> makeDtoEntity = new ArrayList<>();
        for (Board board : reviewBoardAndMainPage) {
            if (board.isAdd()) {
                makeDtoEntity.add(board);
            }
        }
        List<MainPageReviewResponseDTO> dto = new ArrayList<>();
        for (Board board : makeDtoEntity) {
            MainPageReviewResponseDTO temp = new MainPageReviewResponseDTO(board);
            dto.add(temp);
        }
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/data")
    public ResponseEntity<?> readData() {
        try {
            log.info("ENTER /main/data");
        } catch (NullPointerException ne) {
            return ResponseEntity.badRequest().body("NO USER");
        }
        MainPageEntity entity = mainPageService.getById();
        log.info("LEAVE /main/data");
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
            entity.setContact(dto.getContact());
            entity.setImagePath(dto.getImagePath());
            entity.setEmploymentRate(dto.getEmploymentRate());
            entity.setShowEmployment(dto.getShowEmployment());

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
                    .contact(dto.getContact())
                    .passAnnouncementDate(dto.getPassAnnouncementDate())
                    .recruitmentDate(dto.getRecruitmentDate())
                    .imagePath(dto.getImagePath())
                    .employmentRate(dto.getEmploymentRate())
                    .showEmployment(dto.getShowEmployment())
                    .build();
            MainPageEntity saveData = mainPageService.saveData(entity);
            log.info("LEAVE /main/admin-modify - Accessor : "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(saveData);
        }
    }
}
