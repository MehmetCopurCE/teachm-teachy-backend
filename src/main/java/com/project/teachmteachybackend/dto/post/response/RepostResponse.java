package com.project.teachmteachybackend.dto.post.response;

import com.project.teachmteachybackend.entities.Post;
import lombok.Data;

import java.util.Date;
import java.util.Optional;


@Data
public class RepostResponse {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String content;
    //private Date createdAt;
    private Date createdAt;


    public RepostResponse(Optional<Post> entity){
        this.id = entity.get().getId();
        this.userId = entity.get().getUser().getId();
        this.username = entity.get().getUser().getUsername();
        this.title = entity.get().getTitle();
        this.content = entity.get().getContent();
        this.createdAt = entity.get().getCreated_at();
    }

//    public Post toPost(UserService userService){
//        User user = userService.getUserById(this.id);
//        if(user == null)
//            return null;
//        Post post = new Post();
//        post.setId(this.id);
//        post.setTitle(this.title);
//        post.setContent(this.content);
//        post.setUser(user);
//        post.setCreated_at(this.createdAt);
//        return post;
//    }
}
