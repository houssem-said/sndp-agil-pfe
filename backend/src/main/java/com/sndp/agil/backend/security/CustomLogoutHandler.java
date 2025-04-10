package com.sndp.agil.backend.security;

import com.sndp.agil.backend.model.BlacklistedToken;
import com.sndp.agil.backend.repository.BlacklistedTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class CustomLogoutHandler implements LogoutHandler {
    private final JwtUtils jwtUtils;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public CustomLogoutHandler(JwtUtils jwtUtils,
                               BlacklistedTokenRepository blacklistedTokenRepository) {
        this.jwtUtils = jwtUtils;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String token = extractToken(request);
        if (token != null && jwtUtils.validateToken(token)) {
            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(token);
            blacklistedToken.setExpirationDate(new Date(jwtUtils.getExpirationMsFromToken(token)));
            blacklistedTokenRepository.save(blacklistedToken);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}