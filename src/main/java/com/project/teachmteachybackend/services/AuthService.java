package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.refresh.request.RefreshRequest;
import com.project.teachmteachybackend.dto.refresh.response.RefreshResponse;
import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import com.project.teachmteachybackend.dto.user.request.UserLoginRequest;
import com.project.teachmteachybackend.dto.user.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    AuthResponse login(UserLoginRequest userLoginRequest);

    ResponseEntity<AuthResponse> register(UserCreateRequest request);

    ResponseEntity<RefreshResponse> refresh(RefreshRequest refreshRequest);
}
