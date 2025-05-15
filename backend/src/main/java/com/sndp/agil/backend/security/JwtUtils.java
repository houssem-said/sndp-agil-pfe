package com.sndp.agil.backend.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtils {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    // Générer une clé secrète sécurisée avec HS512
    private Key getSigningKey() {
        // Si secretKey est trop courte, on peut la transformer en un Key sécurisé.
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 64) {
            // Assurez-vous que la clé est suffisamment longue pour HS512 (512 bits minimum)
            throw new IllegalArgumentException("La clé secrète est trop courte pour HS512. Veuillez vérifier la configuration.");
        }
        return Keys.hmacShaKeyFor(keyBytes);  // Utilisation de la clé secrète de la configuration, transformée en clé sécurisée
    }

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
                .signWith(getSigningKey())  // Utilisation de la clé correcte pour signer
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
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Utilisation de la clé correcte pour l'analyse
                .build()
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
            System.err.println("Token JWT invalide : " + e.getMessage());
            return false;  // Si une exception est levée, le token est invalide
        }
    }

    // Extraire la date d'expiration du token
    public long getExpirationMsFromToken(String token) {
        return extractClaim(token, Claims::getExpiration).getTime();
    }

}
