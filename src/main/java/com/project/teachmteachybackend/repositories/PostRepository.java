package com.project.teachmteachybackend.repositories;

import com.project.teachmteachybackend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser_Id(Optional<Long> userId);
}
