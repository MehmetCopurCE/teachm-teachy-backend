package com.project.teachmteachybackend.dto.comment.response;

import com.project.teachmteachybackend.dto.post.response.PostResponse;
import com.project.teachmteachybackend.entities.Comment;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.services.PostService;
import com.project.teachmteachybackend.services.Impl.UserServiceImpl;
import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {
    private Long id;
    private Long userId;
    private String username;
    private Long postId;
    private String content;
    //private Date createdAt;
    private Date createdAt;

    public CommentResponse(Comment entity){
        this.id = entity.getId();
        this.content = entity.getContent();
        this.userId = entity.getUser().getId();
        this.username = entity.getUser().getUsername();
        this.postId = entity.getPost().getId();
        this.createdAt = entity.getCreated_at();
    }

    public static Comment toComment(CommentResponse commentResponse, UserServiceImpl userService, PostService postService){
        Comment comment = new Comment();
        User user = userService.getUserById(commentResponse.getUserId());
        PostResponse postResponse = postService.getPostById(commentResponse.getPostId());

        if(user == null || postResponse == null)
            return null;

        comment.setId(commentResponse.getId());
        comment.setUser(user);
        comment.setPost(postResponse.toPost(userService, postResponse.getId()));
        return comment;
    }
}
