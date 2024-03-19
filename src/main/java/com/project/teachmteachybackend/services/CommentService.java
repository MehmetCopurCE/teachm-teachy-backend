package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.comment.request.CommentCreateRequest;
import com.project.teachmteachybackend.dto.comment.request.CommentUpdateRequest;
import com.project.teachmteachybackend.dto.comment.response.CommentResponse;
import com.project.teachmteachybackend.dto.post.response.PostResponse;
import com.project.teachmteachybackend.entities.Comment;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    public CommentResponse createComment(CommentCreateRequest createRequest) {
        PostResponse postResponse = postService.getPostById(createRequest.getPostId());
        User user = userService.getUserById(createRequest.getUserId());

        if(postResponse != null && user != null){
            Comment toSave = new Comment();
            toSave.setContent(createRequest.getContent());
            toSave.setUser(user);
            toSave.setPost(postResponse.toPost(userService));
            //toSave.setCreated_at(new Date());
            toSave.setCreated_at(LocalDateTime.now());
            return new CommentResponse(commentRepository.save(toSave));
        }
        return null;
    }

    public List<CommentResponse> getCommentsWithParams(Optional<Long> userId, Optional<Long> postId) {
        List<Comment> list;
        if(userId.isPresent() && postId.isPresent()){
            list = commentRepository.findByUserIdAndPostId(userId, postId);
        }else if(userId.isPresent()){
            list = commentRepository.findByUserId(userId);
        } else if (postId.isPresent()) {
            list = commentRepository.findByPostId(postId);
        }else{
            list = commentRepository.findAll();
        }
        return list.stream().map(CommentResponse::new).collect(Collectors.toList());
    }

    public CommentResponse getCommentById(Long commentId) {
        Comment comment =  commentRepository.findById(commentId).orElse(null);
        return (comment != null) ? new CommentResponse(comment) : null;
    }

    public Optional<CommentResponse> updateComment(Long commentId, CommentUpdateRequest updateRequest) {
        return commentRepository.findById(commentId).map(existingComment ->{
            existingComment.setContent(updateRequest.getContent());
            return new CommentResponse(commentRepository.save(existingComment));
        });
    }

    public Boolean deleteComment(Long commentId) {
        return commentRepository.findById(commentId).map(comment -> {
            commentRepository.delete(comment);
            return true;
        }).orElse(false);
    }
}
