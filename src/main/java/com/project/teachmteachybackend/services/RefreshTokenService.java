package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.entities.RefreshToken;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh.token.expires.in}")
    Long expireSeconds;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createRefreshToken(Optional<User> user) {
        RefreshToken token = refreshTokenRepository.findByUserId(user.get().getId());
        if(token == null) {
            token =	new RefreshToken();
            token.setUser(user.get());
        }
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public boolean isRefreshExpired(RefreshToken token) {
        return token.getExpiryDate().before(new Date());
    }

    public RefreshToken getByUser(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
}
