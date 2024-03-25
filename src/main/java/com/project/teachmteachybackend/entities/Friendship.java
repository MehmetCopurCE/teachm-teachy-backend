package com.project.teachmteachybackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friendship")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private Long user_id_1;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id_1", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    //private Long user_id_2;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id_2", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user2;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
