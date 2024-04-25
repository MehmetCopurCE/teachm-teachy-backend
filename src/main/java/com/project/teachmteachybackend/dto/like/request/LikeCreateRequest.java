package com.project.teachmteachybackend.dto.like.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LikeCreateRequest {
   private Long userId;
   private Long postId;
}