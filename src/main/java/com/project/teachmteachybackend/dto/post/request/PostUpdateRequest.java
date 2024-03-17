package com.project.teachmteachybackend.dto.post.request;

import lombok.Data;

@Data
public class PostUpdateRequest {
    private String title;
    private String content;
}
