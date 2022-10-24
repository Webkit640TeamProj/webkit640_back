package com.example.webkit640.config;
import com.example.webkit640.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NM8JPctFuna59f5";
    public int validateAndGetUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return Integer.parseInt(claims.getSubject());
    }
    public String create(Member member) {
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(Integer.toString(member.getId())).setIssuer("Webkit640").setIssuedAt(new Date()).setExpiration(expireDate).compact();
    }
}
