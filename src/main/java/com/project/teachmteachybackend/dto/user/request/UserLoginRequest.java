package com.project.teachmteachybackend.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotNull
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 16, message = "User name must be between 2 and 16 characters long")
    private String username;

    @NotNull
    @NotBlank(message = "Şifre cannot be empty")
    @Size(min = 5, max = 20, message = "The password must be between 5 and 20 characters long")
    private String password;
}
