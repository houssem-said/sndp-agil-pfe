package com.sndp.agil.backend.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire des réclamations spécifiques du token (extraction du sujet ici)
    public <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.resolve(claims);
    }

    // Extraire toutes les réclamations du token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Ajouter la méthode parseToken
    public Claims parseToken(String token) {
        return extractAllClaims(token);
    }

    // Vérifier si le token est valide
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);  // Si l'extraction réussit, le token est valide
            return true;
        } catch (JwtException e) {
            return false;  // Si une exception est levée, le token est invalide
        }
    }

    // Extraire la date d'expiration du token
    public long getExpirationMsFromToken(String token) {
        return extractClaim(token, Claims::getExpiration).getTime();
    }

    // Interface pour résoudre des réclamations
    private interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}
