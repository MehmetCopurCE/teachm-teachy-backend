package com.project.teachmteachybackend.repositories;

import com.project.teachmteachybackend.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsBySenderIdAndReceiverId(Long id, Long id1);
}
