package com.project.teachmteachybackend.dto.post.response;

import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.services.UserService;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PostResponse {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String content;
    //private Date createdAt;
    private LocalDateTime createdAt;

    public PostResponse(Post entity){
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.username = entity.getUser().getUsername();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.createdAt = entity.getCreated_at();
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
