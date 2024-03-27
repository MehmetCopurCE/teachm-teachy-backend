package com.project.teachmteachybackend.controllers;

import com.project.teachmteachybackend.dto.comment.request.CommentCreateRequest;
import com.project.teachmteachybackend.dto.comment.request.CommentUpdateRequest;
import com.project.teachmteachybackend.dto.comment.response.CommentResponse;
import com.project.teachmteachybackend.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ./comments
 * ./comments?userId={userId}
 * ./comments?postId={postId}
 * ./comments?userId={userId}&postId={postId}
 * ./comments/{commentId}
 */

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentCreateRequest createRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return ResponseEntity.ok(commentService.createComment(createRequest));
    }


    /**
     * ./comments
     * ./comments?userId={userId}
     * ./comments?postId={postId}
     * ./comments?userId={userId}&postId={postId}
     */
    @GetMapping
    public List<CommentResponse> getCommentsWithParams(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId){
        return commentService.getCommentsWithParams(userId, postId);
    }

    /**
     * ./comments/{commentId}
     */
    @GetMapping("/{commentId}")
    public CommentResponse getCommentById(@PathVariable Long commentId){
        return commentService.getCommentById(commentId);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody @Valid CommentUpdateRequest updateRequest){
        return commentService.updateComment(commentId, updateRequest).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
         if(commentService.deleteComment(commentId)){
             return ResponseEntity.ok().build();
         }
         return ResponseEntity.notFound().build();
    }
}
