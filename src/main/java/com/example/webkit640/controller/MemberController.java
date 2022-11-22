package com.example.webkit640.controller;

import com.example.webkit640.config.EncryptConfig;
import com.example.webkit640.config.TokenProvider;
import com.example.webkit640.dto.LoginDTO;
import com.example.webkit640.dto.request.MemberRequestDTO;
import com.example.webkit640.dto.response.FindLoginUserResonseDTO;
import com.example.webkit640.dto.response.MemberResponseDTO;
import com.example.webkit640.dto.response.ResponseDTO;
import com.example.webkit640.dto.SignUpDTO;
import com.example.webkit640.entity.Member;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class MemberController {
    private final MemberService userService;
    private final TokenProvider tokenProvider;
    private final EncryptConfig encryptConfig;

    @Autowired
    public MemberController(MemberService userService, TokenProvider tokenProvider, EncryptConfig encryptConfig) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.encryptConfig = encryptConfig;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO userDTO) {
        try {
            Member checkMember = userService.findByEmailData(userDTO.getEmail());
            if (checkMember == null) {
                log.info("Enter Register User");
                Member member = Member.builder()
                        .email(userDTO.getEmail())
                        .memberBelong(userDTO.getMemberBelong())
                        .memberType(userDTO.getMemberType())
                        .name(userDTO.getName())
                        .applicant(null)
                        .isAdmin(false)
                        .password(encryptConfig.makeMD5(userDTO.getPassword()))
                        .build();
                Member registeredMember = userService.createMember(member);
                log.info("Create User : "+member.getEmail());
                List<HashMap<String,String>> response = new ArrayList<>();
                HashMap<String,String> data = new HashMap<>();
                data.put("responseCode","200");
                data.put("createEmail", registeredMember.getEmail());
                response.add(data);

                ResponseDTO responseDTO = ResponseDTO.<HashMap<String,String>>builder().data(response).build();
                log.info("SIGN-UP END");
                return ResponseEntity.ok().body(responseDTO);
            } else {
                log.info("EXIST USER" + checkMember.getEmail());
                ResponseDTO responseDTO = ResponseDTO.builder().error("exist user").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch (Exception e) {
            log.error("SIGN-UP Exception: "+e.getMessage());
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDTO loginDTO) {
        log.info("ENTER LOGIN CONTROLLER");
        Member member = userService.getByCredentials(loginDTO.getEmail(), encryptConfig.makeMD5(loginDTO.getPassword()));
        if (member != null) {
            final String token = tokenProvider.create(member);
            final LoginDTO responseData = LoginDTO.builder()
                    .email(member.getEmail())
                    .is_admin(member.isAdmin())
                    .token(token)
                    .memberType(member.getMemberType())
                    .memberBelong(member.getMemberBelong())
                    .name(member.getName())
                    .build();
            log.info("LOGIN-SUCCESS : "+member.getEmail());
            List<LoginDTO> datalist = new ArrayList<>();
            datalist.add(responseData);
            ResponseDTO response = ResponseDTO.<LoginDTO>builder().data(datalist).build();
            log.info("LEAVE LOGIN CONTROLLER");
            return ResponseEntity.ok().body(response);
        } else {
            log.error("LOGIN CONTROLLER ERROR");
            ResponseDTO response = ResponseDTO.builder().error("Email or Password Error").build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/view-members")
    public ResponseEntity<?> allMember(@AuthenticationPrincipal int id) {
        log.info("ENTER VIEW ALL MEMBERS - Accessor : "+ userService.findByid(id).getEmail());
        List<Member> members = userService.getAllMembers();
        List<MemberResponseDTO> dtos = new ArrayList<>();
        for(Member member : members) {
            MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                    .email(member.getEmail())
                    .isAdmin(member.isAdmin())
                    .memberBelong(member.getMemberBelong())
                    .memberType(member.getMemberType())
                    .name(member.getName())
                    .build();
            dtos.add(memberResponseDTO);
        }
        ResponseDTO response = ResponseDTO.<MemberResponseDTO>builder().data(dtos).build();
        log.info("LEAVE VIEW ALL MEMBERRS - Accessor : "+userService.findByid(id).getEmail());
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/admin-change")
    public ResponseEntity<?> changeAdmin(@AuthenticationPrincipal int id, @RequestBody MemberRequestDTO dto) {
        log.info("ENTER ADMIN CHANGE - Accessor : "+userService.findByid(id).getEmail());
        Member member = userService.findByEmailData(dto.getEmail());
        member.setAdmin(!member.isAdmin());
        userService.save(member);
        List<String> data = new ArrayList<>();
        data.add("OK");
        ResponseDTO response = ResponseDTO.<String>builder().data(data).build();
        log.info("LEAVE ADMIN CHANGE - Accessor : "+userService.findByid(id).getEmail());
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/find-user")
    public ResponseEntity<?> findUser(@AuthenticationPrincipal int id) {
        log.info("ENTER /auth/find-user");
        Member member = userService.findByid(id);
        MemberResponseDTO dto = MemberResponseDTO.builder()
                .isAdmin(member.isAdmin())
                .build();
        log.info("LEAVE /auth/find-user");
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/find-login-user")
    public ResponseEntity<?> findLoginUser(@AuthenticationPrincipal int id) {
        try {
            log.info("ENTER /auth/find-login-user - Accessor : " + userService.findByid(id).getEmail());
        } catch (NullPointerException ne) {
            log.error("NULL EXCEPTION /auth/find-login-user - Accessor : " + userService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body("NULL USER");
        }
        Member member = userService.findByid(id);
        FindLoginUserResonseDTO dto = FindLoginUserResonseDTO.builder()
                .admin(member.isAdmin())
                .email(member.getEmail())
                .name(member.getName())
                .build();
        log.info("LEAVE /auth/find-login-user - Accessor : " + member.getEmail());
        return ResponseEntity.ok().body(dto);
    }
}
