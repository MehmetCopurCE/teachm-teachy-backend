package com.project.teachmteachybackend.repositories;


import com.project.teachmteachybackend.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;




    public interface LikeRepository extends JpaRepository<Like, Long> {
        List<Like> findByUserIdAndPostId(Optional<Long> userId, Optional<Long> postId);

        List<Like> findByUserId(Optional<Long> userId);

        List<Like> findByPostId(Optional<Long> postId);
    }


