package com.project.teachmteachybackend.dto.post.response;

import com.project.teachmteachybackend.dto.like.response.LikeResponse;
import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.services.UserService;
import lombok.Data;

import java.time.LocalDateTime;
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
    private List<LikeResponse> postLikes;

    public PostResponse(Post entity, List<LikeResponse> postLikes){
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.username = entity.getUser().getUsername();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.createdAt = entity.getCreated_at();
        this.postLikes = postLikes;
    }

    public Post toPost(UserService userService){
        User user = userService.getUserById(this.id);
        if(user == null)
            return null;
        Post post = new Post();
        post.setId(this.id);
        post.setTitle(this.title);
        post.setContent(this.content);
        post.setUser(user);
        post.setCreated_at(this.createdAt);
        return post;
    }

}
