package com.project.teachmteachybackend.dto.friend;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendResponse {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
}
