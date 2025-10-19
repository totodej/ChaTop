package com.project.chatop.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.project.chatop.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	private final String SECRET_KEY = "MaCleSuperLongueEtComplexePourJWTs2025tChatopu123456";

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10h
	
	public String generateToken(User user) {
        return Jwts.builder()
                .claim("userId", user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null)
                .claim("updatedAt", user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
	
	public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
	
	public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
	
	

}
