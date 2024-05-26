package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.entities.RefreshToken;
import com.project.teachmteachybackend.entities.User;

import java.util.Optional;

public interface RefreshTokenService {
    String createRefreshToken(Optional<User> user);

    RefreshToken getByUser(Long userId);

    boolean isRefreshExpired(RefreshToken token);
}
