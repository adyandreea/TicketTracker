package com.andreea.ticket_tracker.security.config;

import com.andreea.ticket_tracker.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(User user) {
        return createToken(user.getUsername(), user.getRole().name());
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        return createToken(username, role);
    }

    private String createToken(String username, String role) {
        Date currentDate = new Date();
        long JWT_EXPIRATION_TIME = 3600000 * 24 * 2;
        Date expiringDate = new Date(currentDate.getTime() + JWT_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", role)
                .setIssuedAt(currentDate)
                .setExpiration(expiringDate)
                .signWith(getSigningKey(), HS512)
                .compact();
    }

    public String getUsernameFromJWT(final String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            log.info("JWT is valid.");
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT Token is not valid, it could be because it's expired or incorrect.");
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}