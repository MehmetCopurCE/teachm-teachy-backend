package com.project.teachmteachybackend.controllers;

import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import com.project.teachmteachybackend.dto.user.request.UserLoginRequest;
import com.project.teachmteachybackend.dto.user.response.AuthResponse;
import com.project.teachmteachybackend.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
}
