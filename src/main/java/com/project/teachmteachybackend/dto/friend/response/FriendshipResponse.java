package com.project.teachmteachybackend.dto.friend.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FriendshipResponse {

    private String message;
    private HttpStatus status;

    public FriendshipResponse() {
    }

    public FriendshipResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
