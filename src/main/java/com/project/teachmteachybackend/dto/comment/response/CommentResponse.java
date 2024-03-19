package com.project.teachmteachybackend.dto.comment.response;

import com.project.teachmteachybackend.dto.post.response.PostResponse;
import com.project.teachmteachybackend.entities.Comment;
import com.project.teachmteachybackend.entities.Post;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.services.PostService;
import com.project.teachmteachybackend.services.UserService;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CommentResponse {
    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    //private Date createdAt;
    private LocalDateTime createdAt;

    public CommentResponse(Comment entity){
        this.id = entity.getId();
        this.content = entity.getContent();
        this.userId = entity.getUser().getId();
        this.postId = entity.getPost().getId();
        this.createdAt = entity.getCreated_at();
    }

    public static Comment toComment(CommentResponse commentResponse, UserService userService, PostService postService){
        Comment comment = new Comment();
        User user = userService.getUserById(commentResponse.getUserId());
        PostResponse postResponse = postService.getPostById(commentResponse.getPostId());

        if(user == null || postResponse == null)
            return null;

        comment.setId(commentResponse.getId());
        comment.setUser(user);
        comment.setPost(postResponse.toPost(userService));
        return comment;
    }
}
