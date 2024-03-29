package com.example.webkit640.controller;

import com.example.webkit640.config.TokenProvider;
import com.example.webkit640.dto.KakaoDTO;
import com.example.webkit640.dto.LoginDTO;
import com.example.webkit640.dto.OauthResponseDTO;
import com.example.webkit640.dto.response.ResponseDTO;
import com.example.webkit640.entity.Member;
import com.example.webkit640.service.KakaoOauthService;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/auth/oauth")
public class OauthUserController {

    private final KakaoOauthService kakaoOauthService;
    private final MemberService userService;
    private final TokenProvider tokenProvider;

    @Autowired
    public OauthUserController(KakaoOauthService kakaoOauthService, MemberService userService, TokenProvider tokenProvider) {
        this.kakaoOauthService = kakaoOauthService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoGetToken(@RequestParam String code) {
        log.info("ENTER /auth/oauth/kakao");
        //Kakao Login 수행 과정
        String accessToken = kakaoOauthService.getKakaoAccessToken(code);
        KakaoDTO kakaoDTO = kakaoOauthService.createKakaoUser(accessToken);

        //로그인한 카카오 이메일이 로컬 계정으로 등록되어있다면
        if (userService.findByEmail(kakaoDTO.getEmail())) {

            //로컬 멤버 데이터 추출 후 JWT 토큰 추가하여 프론트엔드로 반환
            Member member = userService.getByEmail(kakaoDTO.getEmail());
            //멤버 못가져올경우 예외처리 조건문
            if (member != null) {
                final String token = tokenProvider.create(member); //토큰 생성
                final LoginDTO responseUserDTO = LoginDTO.builder() //프론트로 반환할 DTO 생성
                        .email(member.getEmail())
                        .name(member.getName())
                        .memberBelong(member.getMemberBelong())
                        .id(member.getId())
                        .token(token)
                        .memberType(member.getMemberType())
                        .build();

                List<LoginDTO> dtos = new ArrayList<>();
                dtos.add(responseUserDTO);
                ResponseDTO response = ResponseDTO.<LoginDTO>builder().data(dtos).build();
                log.info("LEAVE /auth/oauth/kakao - LOGIN SUCCESS");
                return ResponseEntity.ok().body(response);
            } else {
                log.error("ERROR /auth/oauth/kakao - LOGIN FAILED");
                ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } else {
            //로컬 미등록 계정
            List<KakaoDTO> dtos = new ArrayList<>();
            dtos.add(kakaoDTO);
            OauthResponseDTO responseDTO = OauthResponseDTO.<KakaoDTO>builder().isUser(false).error("No Local User").data(dtos).build();
            log.error("ERROR /auth/oauth/kakao - NOT REGISTERED USER");
            return ResponseEntity.ok().body(responseDTO);
        }
    }
}
