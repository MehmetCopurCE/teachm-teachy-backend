package com.project.teachmteachybackend.controllers;

import com.project.teachmteachybackend.dto.post.response.PostResponse;
import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.dto.post.request.PostCreateRequest;
import com.project.teachmteachybackend.dto.post.request.PostUpdateRequest;
import com.project.teachmteachybackend.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 ./api/posts
 ./api/posts?userId={userId}
 ./api/posts/{postId}
 */

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * ./posts
     * ./posts?userId={userId}
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Optional<Long> userId){
        return ResponseEntity.ok(postService.getAllPosts(userId));
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Valid PostCreateRequest postCreateRequest, BindingResult bindingResult){
        //return new ResponseEntity<>(postService.createPost(post), HttpStatus.CREATED);
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        PostResponse postResponse =  postService.createPost(postCreateRequest);
        return ResponseEntity.ok(postResponse);
    }

    /**
     * ./posts/{postId}
     */
    @GetMapping("/{postId}")
    public PostResponse getPostById(@PathVariable Long postId){
        //return postService.getPostById(postId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        return postService.getPostById(postId);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody @Valid PostUpdateRequest updateRequest){
        return postService.updatePost(postId,updateRequest).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        if(postService.deletePost(postId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
