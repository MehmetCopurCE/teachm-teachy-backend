package com.project.teachmteachybackend.services;


import com.project.teachmteachybackend.dto.like.request.LikeCreateRequest;

import com.project.teachmteachybackend.dto.like.response.LikeResponse;
import com.project.teachmteachybackend.dto.post.response.PostResponse;
import com.project.teachmteachybackend.entities.Like;
import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.entities.User;

import com.project.teachmteachybackend.repositories.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

import java.util.stream.Collectors;







@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;
    private Post post;

    public LikeService(LikeRepository likeRepository, PostService postService, UserService userService) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.userService = userService;
    }


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




    public Like createOneLike(LikeCreateRequest request) {
        User user = userService.getUserById(request.getUserId());
        PostResponse postResponse = postService.getPostById(request.getPostId());

        if (user != null && postResponse != null) {
            Like likeToSave = new Like();
            likeToSave.setUser(user);
            likeToSave.setPost(post);
            return likeRepository.save(likeToSave);
        } else {
            return null;
        }
    }



    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }


    public void getOneLikeById(Long likeId) {

        likeRepository.findById(likeId);
    }
}
