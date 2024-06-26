package com.project.teachmteachybackend.controllers;

import com.project.teachmteachybackend.dto.refresh.request.RefreshRequest;
import com.project.teachmteachybackend.dto.refresh.response.RefreshResponse;
import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import com.project.teachmteachybackend.dto.user.request.UserLoginRequest;
import com.project.teachmteachybackend.dto.user.response.AuthResponse;
import com.project.teachmteachybackend.services.AuthService;
import com.project.teachmteachybackend.services.Impl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ./api/auth/register
 * ./api/auth/login
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        return new ResponseEntity<>(authService.login(userLoginRequest), HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid UserCreateRequest request){
        return authService.register(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshRequest refreshRequest){
        return authService.refresh(refreshRequest);
    }
}
