package com.project.teachmteachybackend.dto.friend.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendshipRequest {
    @NotNull(message = "Sender userId cannot be empty")
    private Long senderId;

    @NotNull(message = "Receiver userId cannot be empty")
    private Long receiverId;
}
