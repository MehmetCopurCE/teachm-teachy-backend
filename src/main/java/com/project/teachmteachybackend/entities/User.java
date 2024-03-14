package com.project.teachmteachybackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String userName;

    @NotBlank(message = "Şifre boş olamaz")
    private String password;

    @NotBlank(message = "İsim boş olamaz")
    private String firstName;

    @NotBlank(message = "Soyisim boş olamaz")
    private String lastName;

    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @NotBlank(message = "E-posta boş olamaz")
    private String email;

    private Timestamp created_at;

    @NotEmpty(message = "En az bir role seçilmelidir")
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0")
    private double userStatistic;

}
