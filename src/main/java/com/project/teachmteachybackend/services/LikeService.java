package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.like.request.LikeCreateRequest;
import com.project.teachmteachybackend.dto.like.response.LikeResponse;
import com.project.teachmteachybackend.entities.Like;

import java.util.List;
import java.util.Optional;

public interface LikeService {
    List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId);

    Like createOneLike(LikeCreateRequest request);

    void deleteOneLikeById(Long likeId);

    Optional<Like> getOneLikeById(Long likeId);

}
