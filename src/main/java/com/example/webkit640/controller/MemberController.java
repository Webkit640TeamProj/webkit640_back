package com.example.webkit640.controller;

import com.example.webkit640.config.TokenProvider;
import com.example.webkit640.dto.LoginDTO;
import com.example.webkit640.dto.ResponseDTO;
import com.example.webkit640.dto.SignUpDTO;
import com.example.webkit640.entity.Member;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class MemberController {
    @Autowired
    private MemberService userService;
    @Autowired
    private TokenProvider tokenProvider;
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO userDTO) {
        try {
            Member member = Member.builder()
                    .email(userDTO.getEmail())
                    .memberBelong(userDTO.getMemberBelong())
                    .memberType(userDTO.getMemberType())
                    .name(userDTO.getName())
                    .applicant(null)
                    .password(userDTO.getPassword())
                    .build();
            Member registeredMember = userService.createMember(member);
            /*
            * TODO: [송민규]: 계정 생성 로그 추가 필요
            */
            List<HashMap<String,String>> response = new ArrayList<>();
            HashMap<String,String> data = new HashMap<>();
            data.put("responseCode","200");
            data.put("createEmail", registeredMember.getEmail());
            response.add(data);

            ResponseDTO responseDTO = ResponseDTO.<HashMap<String,String>>builder().data(response).build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDTO loginDTO) {
        Member member = userService.getByCredentials(loginDTO.getEmail(), loginDTO.getPassword());
        if (member != null) {
            final String token = tokenProvider.create(member);
            final LoginDTO responseData = LoginDTO.builder()
                    .email(member.getEmail())
                    .token(token)
                    .memberType(member.getMemberType())
                    .memberBelong(member.getMemberBelong())
                    .build();
            /**
             * TODO: [송민규], 계정 생성 로그 추가 로직 필요함
             */
            List<LoginDTO> datalist = new ArrayList<>();
            datalist.add(responseData);
            ResponseDTO response = ResponseDTO.<LoginDTO>builder().data(datalist).build();
            return ResponseEntity.ok().body(response);
        } else {
            ResponseDTO response = ResponseDTO.builder().error("Email or Password Error").build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
