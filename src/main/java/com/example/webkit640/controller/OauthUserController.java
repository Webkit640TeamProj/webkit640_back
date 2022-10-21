package com.example.webkit640.controller;

import com.example.webkit640.entity.KakaoDTO;
import com.example.webkit640.entity.ResponseDTO;
import com.example.webkit640.service.KakaoOauthService;
import com.example.webkit640.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth/oauth")
public class OauthUserController {
    private final KakaoOauthService kakaoOauthService;
    private final UserService userService;

    @Autowired
    public OauthUserController(KakaoOauthService kakaoOauthService, UserService userService) {
        this.kakaoOauthService = kakaoOauthService;
        this.userService = userService;
    }

    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoGetToken(@RequestParam String code) {
        log.info("code : " + code);
        String accessToken = kakaoOauthService.getKakaoAccessToken(code);
        log.info("Access_token : " + accessToken);
        KakaoDTO kakaoDTO = kakaoOauthService.createKakaoUser(accessToken);
        if (userService.findByEmail(kakaoDTO.getEmail())) {
            return ResponseEntity.ok().body(kakaoDTO.getEmail());
        } else {
            List<KakaoDTO> dtos = new ArrayList<>();
            dtos.add(kakaoDTO);
            ResponseDTO responseDTO = ResponseDTO.<KakaoDTO>builder().isUser(false).error("No Local User").data(dtos).build();
            return ResponseEntity.ok().body(responseDTO);
        }
    }
}
