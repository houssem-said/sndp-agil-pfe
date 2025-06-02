package com.sndp.agil.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utilitaires pour générer, valider et parser les JSON Web Tokens (JWT).
 */
@Component
public class JwtUtils {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    /**
     * Construit la clé de signature HMAC-SHA à partir du secretKey configuré.
     */
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 64) {
            throw new IllegalArgumentException("La clé secrète est trop courte pour HS512. Vérifiez la configuration.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Génère un JWT à partir des informations de l’utilisateur (roles, subject=email).
     * Le token contient un claim "roles" contenant la liste des rôles (ex ["ROLE_CLIENT"]).
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extrait toutes les revendications (claims) depuis le token.
     * Lance une exception si le token est invalide ou expiré.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Récupère le sujet (subject) du token, qui correspond ici à l’email de l’utilisateur.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Valide le token : si l’extraction des claims échoue (expiration, signature invalide), renvoie false.
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            System.err.println("Token JWT invalide : " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère la date d’expiration du token (en millisecondes depuis epoch).
     */
    public long getExpirationMsFromToken(String token) {
        return extractAllClaims(token).getExpiration().getTime();
    }

    /**
     * Parse le token et retourne l’objet Claims complet.
     */
    public Claims parseToken(String token) {
        return extractAllClaims(token);
    }
}
