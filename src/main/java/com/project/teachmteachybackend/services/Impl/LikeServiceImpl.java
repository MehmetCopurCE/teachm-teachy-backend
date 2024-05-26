package com.project.teachmteachybackend.services.Impl;

import com.project.teachmteachybackend.dto.like.request.LikeCreateRequest;

import com.project.teachmteachybackend.dto.like.response.LikeResponse;
import com.project.teachmteachybackend.dto.post.response.PostResponse;
import com.project.teachmteachybackend.entities.Like;
import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.entities.User;

import com.project.teachmteachybackend.repositories.LikeRepository;
import com.project.teachmteachybackend.services.LikeService;
import com.project.teachmteachybackend.services.PostService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    @Lazy
    private final PostService postService;
    private final UserServiceImpl userService;


    public LikeServiceImpl(LikeRepository likeRepository, @Lazy PostService postService, UserServiceImpl userService) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.userService = userService;
    }


    @Override
    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
        List<Like> list;
        if(userId.isPresent() && postId.isPresent()) {
            list = likeRepository.findByUserIdAndPostId(Optional.of(userId.get()), Optional.of(postId.get()));//optional .of yazınca düzeliyor..
        }else if(userId.isPresent()) {
            list = likeRepository.findByUserId(Optional.of(userId.get()));
        }else if(postId.isPresent()) {
            list = likeRepository.findByPostId(Optional.of(postId.get()));
        }else
            list = likeRepository.findAll();
        return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());

    }



    @Override
    public Like createOneLike(LikeCreateRequest request) {
        User user = userService.getUserById(request.getUserId());
        PostResponse postResponse = postService.getPostById(request.getPostId());

        if (user != null && postResponse != null) {
            Like likeToSave = new Like();
            Post post = new Post();
            post .setId(postResponse.getId());
            post.setTitle(postResponse.getTitle());
            post.setContent(postResponse.getContent());
            post.setCreated_at(new Date());
            post.setUser(user);

            likeToSave.setUser(user);
            likeToSave.setPost(post);
            return likeRepository.save(likeToSave);
        } else {
            return null;
        }
    }


    @Override
    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }

    @Override
    public Optional<Like> getOneLikeById(Long likeId) {
        return likeRepository.findById(likeId);
    }


}
