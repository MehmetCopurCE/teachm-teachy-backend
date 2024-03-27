package com.project.teachmteachybackend.dto.user.request;


import com.project.teachmteachybackend.enums.AccountPrivacy;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotNull
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 16, message = "User name must be between 2 and 16 characters long")
    private String username;

    @NotNull
    @NotBlank(message = "Şifre cannot be empty")
    @Size(min = 5, max = 20, message = "The password must be between 5 and 20 characters long")
    private String password;

    @NotNull
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 characters long")
    private String firstName;

    @NotNull
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters long")
    private String lastName;

    @NotNull
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @NotBlank(message = "E-posta boş olamaz")
    private String email;

    @NotNull
    @NotBlank(message = "User question cannot be empty")
    private String question;

    @NotNull
    @NotBlank(message = "User question answer cannot be empty")
    private String answer;

    private AccountPrivacy accountPrivacy = AccountPrivacy.PUBLIC;
}
