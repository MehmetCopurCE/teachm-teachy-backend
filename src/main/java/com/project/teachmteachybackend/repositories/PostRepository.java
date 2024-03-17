package com.project.teachmteachybackend.repositories;

import com.project.teachmteachybackend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
