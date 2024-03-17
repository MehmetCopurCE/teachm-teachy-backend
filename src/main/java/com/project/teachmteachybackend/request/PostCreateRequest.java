package com.project.teachmteachybackend.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostCreateRequest {

    @NotNull(message = "Post userId cannot be empty")
    private Long userId;

    @NotNull
    @NotBlank(message = "Post title cannot be empty")
    @Size(min = 5, message = "The password must be more than 5 characters long")
    private String title;

    private String content;
}
