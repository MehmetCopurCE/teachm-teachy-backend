package com.project.teachmteachybackend.entities;

import com.project.teachmteachybackend.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String question;

    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    private LocalDateTime registrationTime;

    private double userStatistic;

}
