package com.project.teachmteachybackend.dto.user.response;

import lombok.Data;

@Data
public class AuthResponse {

    Long userId;
    String status;
    String message;
    String accessToken;
    //String refreshToken;
}
