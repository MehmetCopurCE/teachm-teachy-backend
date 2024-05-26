package com.project.teachmteachybackend.dto.post.response;

import com.project.teachmteachybackend.dto.like.response.LikeResponse;
import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.services.Impl.UserServiceImpl;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostResponse {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String content;
    //private Date createdAt;
    private Date createdAt;
    private boolean isRepost;
    private RepostResponse originPost;
    private List<LikeResponse> postLikes;

/*    public PostResponse(Post entity, List<LikeResponse> postLikes){
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.username = entity.getUser().getUsername();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.isRepost = entity.isRepost();
        this.createdAt = entity.getCreated_at();
        this.repostResponse = null;
        this.postLikes = postLikes;
    }*/

    public PostResponse(Post entity, List<LikeResponse> postLikes, RepostResponse repostResponse){
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.username = entity.getUser().getUsername();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.isRepost = entity.isRepost();
        this.createdAt = entity.getCreated_at();
        this.originPost = repostResponse;
        this.postLikes = postLikes;
    }

    public Post toPost(UserServiceImpl userService, Long postId){
        User user = userService.getUserById(this.userId);
        if(user == null)
            return null;
        Post post = new Post();
        post.setId(postId);
        post.setTitle(this.title);
        post.setContent(this.content);
        post.setUser(user);
        post.setCreated_at(this.createdAt);
        return post;
    }


}
