package com.project.teachmteachybackend.dto.comment.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentUpdateRequest {
    @NotNull(message = "Comment content cannot be empty")
    private String content;
}
