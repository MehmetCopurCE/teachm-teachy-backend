package com.project.teachmteachybackend.services;


import com.project.teachmteachybackend.dto.post.response.PostResponse;
import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.repositories.PostRepository;
import com.project.teachmteachybackend.dto.post.request.PostCreateRequest;
import com.project.teachmteachybackend.dto.post.request.PostUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if(userId.isPresent())
            list = postRepository.findByUser_Id(userId);
        else
            list = postRepository.findAll();
        return list.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public PostResponse createPost(PostCreateRequest createRequest) {
        User user = userService.getUserById(createRequest.getUserId());
        if(user == null)
            return null; //TODO Burada bir hata mesajıda dönderebilirsin

        Post toSave = new Post();
        toSave.setUser(user);
        toSave.setTitle(createRequest.getTitle());
        toSave.setContent(createRequest.getContent());
        toSave.setCreated_at(new Date());
        return new PostResponse(postRepository.save(toSave));
    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        return (post != null) ? new PostResponse(post) : null;
    }

    public Optional<PostResponse> updatePost(Long postId, PostUpdateRequest updateRequest) {
        return postRepository.findById(postId).map(existingPost ->{
            existingPost.setTitle(updateRequest.getTitle());
            existingPost.setContent(updateRequest.getContent());
            return new PostResponse(postRepository.save(existingPost));
        });
    }

    public boolean deletePost(Long postId) {
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return true;
        }).orElse(false);
    }
}
