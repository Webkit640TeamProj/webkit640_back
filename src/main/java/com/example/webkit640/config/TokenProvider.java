package com.example.webkit640.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NM8JPctFuna59f5";
    public int validateAndGetUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return Integer.parseInt(claims.getSubject());
    }
}
