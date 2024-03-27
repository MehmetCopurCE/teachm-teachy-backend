package com.project.teachmteachybackend.dto.friend.response;

import com.project.teachmteachybackend.entities.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class FriendResponse {
    private Long friendId;
    private String friendUsername;
    private String friendFirstName;
    private String friendLastName;
    private LocalDateTime createdAt;

    public FriendResponse(User entity){
        this.friendId = entity.getId();
        this.createdAt = entity.getRegistrationTime();
        this.friendUsername = entity.getUsername();
        this.friendFirstName = entity.getFirstName();
        this.friendLastName = entity.getLastName();
    }
}
