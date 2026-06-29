package com.supermarket.management.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Must be at least 256 bits (32 bytes) long
    private final String secretString = "supermarket-management-secret-key-2026-freshmart-secure-token-256bits";
    private final Key key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    private final long validityInMilliseconds = 86400000; // 24 Hours

    public String createToken(String username, String role, String businessName) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put("businessName", businessName);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getBusinessName(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("businessName", String.class);
    }
}
