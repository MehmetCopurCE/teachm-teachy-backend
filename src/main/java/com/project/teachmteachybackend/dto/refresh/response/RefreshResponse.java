package com.project.teachmteachybackend.dto.refresh.response;

import lombok.Data;

@Data
public class RefreshResponse {
    private Long userId;
    private String message;
    private String accessToken;
}
