package com.project.teachmteachybackend.controllers;

import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.request.PostCreateRequest;
import com.project.teachmteachybackend.request.PostUpdateRequest;
import com.project.teachmteachybackend.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Valid PostCreateRequest postCreateRequest, BindingResult bindingResult){
        //return new ResponseEntity<>(postService.createPost(post), HttpStatus.CREATED);
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Post post =  postService.createPost(postCreateRequest);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId){
        return postService.getPostById(postId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody @Valid PostUpdateRequest updateRequest){
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
