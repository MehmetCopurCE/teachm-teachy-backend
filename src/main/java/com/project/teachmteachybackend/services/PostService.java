package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.post.request.PostCreateRequest;
import com.project.teachmteachybackend.dto.post.request.PostUpdateRequest;
import com.project.teachmteachybackend.dto.post.request.RepostCreateRequest;
import com.project.teachmteachybackend.dto.post.response.PostResponse;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<PostResponse> getAllPosts(Optional<Long> userId);

    PostResponse createPost(PostCreateRequest postCreateRequest);

    PostResponse createRePost(RepostCreateRequest repostCreateRequest);

    PostResponse getPostById(Long postId);

    boolean deletePost(Long postId);

    Optional<Object> updatePost(Long postId, PostUpdateRequest updateRequest);
}
