package com.project.teachmteachybackend.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
@Data
public class CommentCreateRequest {
    @NotNull
    @NotBlank(message = "Post title cannot be empty")
    private String content;

    @NotNull(message = "Comment userId cannot be empty")
    private Long userId;

    @NotNull(message = "Comment postId cannot be empty")
    private Long postId;
}
