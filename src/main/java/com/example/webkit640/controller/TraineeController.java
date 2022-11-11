package com.example.webkit640.controller;


import com.example.webkit640.dto.response.TraineeGetAllResponseDTO;
import com.example.webkit640.entity.Member;
import com.example.webkit640.entity.Trainee;
import com.example.webkit640.service.MemberService;
import com.example.webkit640.service.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/trainee")
@Slf4j
public class TraineeController {
    private final TraineeService traineeService;
    private final MemberService memberService;

    @Autowired
    public TraineeController(TraineeService traineeService, MemberService memberService) {
        this.traineeService = traineeService;
        this.memberService = memberService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllTrainee(@AuthenticationPrincipal int id) {
        Member member = null;
        List<Trainee> trainees = null;
        List<TraineeGetAllResponseDTO> dtos = new ArrayList<>();
        try {
            member = memberService.findByid(id);
            log.info("ENTER /trainee/find-all - Accessor : " + member.getEmail());
        } catch (NullPointerException ne) {
            log.error("ACCESS ID NULL /trainee/find-all");
            return ResponseEntity.badRequest().body("ACCESS ID NULL /trainee/find-all");
        }
        try {
            trainees = traineeService.getAllTrainee();
            for (Trainee trainee : trainees) {
                dtos.add(TraineeGetAllResponseDTO.entityToDTO(trainee));
            }
            log.info("LEAVE /trainee/find-all - Accessor : " + member.getEmail());
            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            log.error("EXCEPTION /trainee/find-all - Accessor : " + member.getEmail());
            return ResponseEntity.badRequest().body("EXCEPTION /trainee/find-all - Accessor : " + member.getEmail());
        }
    }
}
