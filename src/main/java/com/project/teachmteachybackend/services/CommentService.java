package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.comment.request.CommentCreateRequest;
import com.project.teachmteachybackend.dto.comment.request.CommentUpdateRequest;
import com.project.teachmteachybackend.dto.comment.response.CommentResponse;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Object createComment(CommentCreateRequest createRequest);

    List<CommentResponse> getCommentsWithParams(Optional<Long> userId, Optional<Long> postId);

    CommentResponse getCommentById(Long commentId);

    Optional<Object> updateComment(Long commentId, CommentUpdateRequest updateRequest);

    boolean deleteComment(Long commentId);
}
