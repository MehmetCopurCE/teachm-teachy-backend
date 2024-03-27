package com.project.teachmteachybackend.dto.follow.response;

import com.project.teachmteachybackend.entities.Follow;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowResponse {
    private Long senderId;
    private LocalDateTime createdAt;
    private String status;

    public FollowResponse(Follow entity){
        this.senderId = entity.getSenderId();
        this.status = String.valueOf(entity.getStatus());
        this.createdAt = entity.getCreatedAt();
    }
}
