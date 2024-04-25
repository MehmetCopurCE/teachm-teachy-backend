package com.project.teachmteachybackend.dto.refresh.request;

import lombok.Data;

@Data
public class RefreshRequest {

    Long userId;
    String refreshToken;
}
