package com.project.teachmteachybackend.services;


import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.repositories.PostRepository;
import com.project.teachmteachybackend.request.PostCreateRequest;
import com.project.teachmteachybackend.request.PostUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(PostCreateRequest createRequest) {
        Post toSave = new Post();
        toSave.setUserId(createRequest.getUserId());
        toSave.setTitle(createRequest.getTitle());
        toSave.setContent(createRequest.getContent());
        toSave.setCreated_at(new Date());
        return postRepository.save(toSave);
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public Optional<Post> updatePost(Long postId, PostUpdateRequest updateRequest) {
        return postRepository.findById(postId).map(existingPost ->{
            existingPost.setTitle(updateRequest.getTitle());
            existingPost.setContent(updateRequest.getContent());
            return postRepository.save(existingPost);
        });
    }

    public boolean deletePost(Long postId) {
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return true;
        }).orElse(false);
    }
}
