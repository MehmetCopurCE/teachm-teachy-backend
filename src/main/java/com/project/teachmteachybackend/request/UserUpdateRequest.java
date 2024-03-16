package com.project.teachmteachybackend.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {
    @NotNull
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String username;

    @NotNull
    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 8, message = "Şifre  en az 8 karakter olmalıdır")
    private String password;

    @NotNull
    @NotBlank(message = "İsim boş olamaz")
    private String firstName;

    @NotNull
    @NotBlank(message = "Soyisim boş olamaz")
    private String lastName;

    @NotNull
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @NotBlank(message = "E-posta boş olamaz")
    private String email;
}
