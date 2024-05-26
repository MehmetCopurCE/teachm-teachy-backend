package com.project.teachmteachybackend.services;
import com.project.teachmteachybackend.dto.like.response.LikeResponse;
import com.project.teachmteachybackend.dto.post.request.RepostCreateRequest;
import com.project.teachmteachybackend.dto.post.response.PostResponse;
import com.project.teachmteachybackend.dto.post.response.RepostResponse;
import com.project.teachmteachybackend.entities.Like;
import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.repositories.LikeRepository;
import com.project.teachmteachybackend.repositories.PostRepository;
import com.project.teachmteachybackend.dto.post.request.PostCreateRequest;
import com.project.teachmteachybackend.dto.post.request.PostUpdateRequest;
import com.project.teachmteachybackend.services.Impl.LikeServiceImpl;
import com.project.teachmteachybackend.services.Impl.UserServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private PostRepository postRepository;
    private UserServiceImpl userService;
    private LikeService likeService;
    private final LikeRepository likeRepository;

    public PostService(PostRepository postRepository, UserServiceImpl userService, LikeServiceImpl likeService, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.likeService = likeService;
        this.likeRepository = likeRepository;
    }


    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if (userId.isPresent())
            list = postRepository.findByUser_Id(userId);
        else
            list = postRepository.findAll();
        return list.stream().map(post -> {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(post.getId()));
            if (post.isRepost()) {
                Post originPost = postRepository.findById(post.getOriginalPost().getId()).orElse(null);
                RepostResponse repostResponse = new RepostResponse(Optional.of(originPost));
                return new PostResponse(post, likes, repostResponse);
            }
            return new PostResponse(post, likes, null);
        }).collect(Collectors.toList());
    }

    public PostResponse createPost(PostCreateRequest createRequest) {
        User user = userService.getUserById(createRequest.getUserId());
        if (user == null)
            return null; //TODO Burada bir hata mesajıda dönderebilirsin

        Post toSave = new Post();
        toSave.setUser(user);
        toSave.setTitle(createRequest.getTitle());
        toSave.setContent(createRequest.getContent());
        toSave.setCreated_at(new Date());
        toSave.setRepost(false);   //ilk post oluşturulurken isRepost u false olarak ayarlıyoruz
        toSave.setOriginalPost(null);
        Post savedPost = postRepository.save(toSave);

        // Fetch the likes for the post and convert them into LikeResponse objects
        List<Like> likes = likeRepository.findByPostId(Optional.ofNullable(savedPost.getId()));
        List<LikeResponse> likeResponses = likes.stream()
                .map(LikeResponse::new)
                .collect(Collectors.toList());

        return new PostResponse(savedPost, likeResponses, null);
    }

    public PostResponse createRePost(RepostCreateRequest repostCreateRequest) {
        User user = userService.getUserById(repostCreateRequest.getUserId());
        if (user == null)
            return null; //TODO Burada bir hata mesajıda dönderebilirsin

        Post post = postRepository.findById(repostCreateRequest.getOriginalPostId()).orElse(null);
        if (post == null)
            return null; //TODO Burada bir hata mesajıda dönderebilirsin

        Post toSave = new Post();
        toSave.setUser(user);
        toSave.setTitle(repostCreateRequest.getTitle());
        toSave.setContent(repostCreateRequest.getContent());
        toSave.setCreated_at(new Date());
        toSave.setRepost(true);   //ilk post oluşturulurken isRepost u false olarak ayarlıyoruz
        toSave.setOriginalPost(post);
        Post savedPost = postRepository.save(toSave);

        // Fetch the likes for the post and convert them into LikeResponse objects
        List<Like> likes = likeRepository.findByPostId(Optional.ofNullable(savedPost.getId()));
        List<LikeResponse> likeResponses = likes.stream()
                .map(LikeResponse::new)
                .collect(Collectors.toList());

        RepostResponse repostResponse = new RepostResponse(Optional.of(post));
        return new PostResponse(savedPost, likeResponses, repostResponse);

    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);

        if(post == null)
            return null;
        List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
        if(post.isRepost()){
            Post originPost = postRepository.findById(post.getOriginalPost().getId()).orElse(null);
            RepostResponse repostResponse = new RepostResponse(Optional.ofNullable(originPost));
            return new PostResponse(post, likes, repostResponse);
        }
        return  new PostResponse(post, likes, null);
    }

    public Optional<PostResponse> updatePost(Long postId, PostUpdateRequest updateRequest) {
        return postRepository.findById(postId).map(existingPost -> {
            existingPost.setTitle(updateRequest.getTitle());
            existingPost.setContent(updateRequest.getContent());
            Post updatedPost = postRepository.save(existingPost);

            // Fetch the likes for the post and convert them into LikeResponse objects
            List<Like> likes = likeRepository.findByPostId(Optional.ofNullable(updatedPost.getId()));
            List<LikeResponse> likeResponses = likes.stream()
                    .map(LikeResponse::new)
                    .collect(Collectors.toList());

            if(existingPost.isRepost()){
                Optional<Post> originPost = postRepository.findById(updatedPost.getOriginalPost().getId());
                RepostResponse repostResponse = new RepostResponse(originPost);
                return new PostResponse(updatedPost, likeResponses, repostResponse);
            }
            return new PostResponse(updatedPost, likeResponses, null);
        });
    }


    public boolean deletePost(Long postId) {
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return true;
        }).orElse(false);
    }



}
