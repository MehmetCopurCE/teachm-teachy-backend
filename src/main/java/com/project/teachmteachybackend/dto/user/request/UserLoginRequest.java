package com.project.teachmteachybackend.dto.user.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
