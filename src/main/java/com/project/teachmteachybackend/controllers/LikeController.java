package com.project.teachmteachybackend.controllers;


import com.project.teachmteachybackend.dto.like.request.LikeCreateRequest;
import com.project.teachmteachybackend.dto.like.response.LikeResponse;
import com.project.teachmteachybackend.entities.Like;
import com.project.teachmteachybackend.services.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
    @RequestMapping("/likes")
    public class LikeController {

        private LikeService likeService;

        public LikeController(LikeService likeService) {
            this.likeService = likeService;
        }

        @GetMapping
        public List<LikeResponse> getAllLikes(@RequestParam Optional<Long> userId,
                                              @RequestParam Optional<Long> postId) {
            return likeService.getAllLikesWithParam(userId, postId);
        }

        @PostMapping
        public Like createOneLike(@RequestBody LikeCreateRequest request) {
            return likeService.createOneLike(request);
        }

        @GetMapping("/{likeId}")
        public Optional<Like> getOneLike(@PathVariable Long likeId) {
            return likeService.getOneLikeById(likeId);
        }

        @DeleteMapping("/{likeId}")
        public void deleteOneLike(@PathVariable Long likeId) {

            likeService.deleteOneLikeById(likeId);
        }
    }

