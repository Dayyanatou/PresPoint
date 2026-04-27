package com.fst.gestionpresencepointage.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clé secrète — garde-la secrète en prod
    private static final String SECRET = "fst-gestion-presence-pointage-secret-key-2024";
    private static final long EXPIRATION = 86400000L; // 24h en millisecondes

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Génère un token JWT pour un utilisateur
    public String genererToken(Long userId, String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrait le username du token
    public String extraireUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey()).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extrait le userId du token
    public Long extraireUserId(String token) {
        return ((Number) Jwts.parserBuilder()
                .setSigningKey(getKey()).build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId")).longValue();
    }

    // Extrait le role du token
    public String extraireRole(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getKey()).build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    // Vérifie si le token est valide
    public boolean estValide(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}