package com.project.teachmteachybackend.dto.post.response;

import com.project.teachmteachybackend.entities.Post;
import lombok.Data;

import java.util.Date;

@Data
public class PostResponse {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private Date createdAt;

    public PostResponse(Post entity){
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.username = entity.getUser().getUsername();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.createdAt = entity.getCreated_at();
    }
}
