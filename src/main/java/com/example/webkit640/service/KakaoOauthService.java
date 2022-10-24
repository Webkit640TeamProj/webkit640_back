package com.example.webkit640.service;

import com.example.webkit640.dto.KakaoDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class KakaoOauthService {
    public String getKakaoAccessToken(String code) {
        String accessToken = "";
        String refreshToken;
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=ceff3c3a5ff411df946f1aa557ffc001" +
                    "&redirect_uri=http://localhost:8080/auth/oauth/kakao" +
                    "&code=" + code;
            bw.write(sb);
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info(String.valueOf(responseCode));

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line="";
            String result="";

            while((line = br.readLine()) != null) {
                result += line;
            }
            log.info(result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("access_token : " + accessToken);
            log.info("refresh_token : " + refreshToken);

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public KakaoDTO createKakaoUser(String token) {
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        String email = "";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization","Bearer "+token);

            int responseCode = conn.getResponseCode();
            log.info("Response Code : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            int id = element.getAsJsonObject().get("id").getAsInt();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();

            if(hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }
            log.info(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        KakaoDTO dto = KakaoDTO.builder().email(email).build();
        return dto;
    }
}
