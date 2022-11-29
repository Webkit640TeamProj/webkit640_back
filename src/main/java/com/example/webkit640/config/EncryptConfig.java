package com.example.webkit640.config;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
public class EncryptConfig {
    public String makeMD5(String password) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] byteData = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i=0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            md5 = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            md5 = null;
        }
        return md5;
    }
}
