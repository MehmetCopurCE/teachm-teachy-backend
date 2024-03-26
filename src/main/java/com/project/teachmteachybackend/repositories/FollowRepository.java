package com.project.teachmteachybackend.repositories;

import com.project.teachmteachybackend.entities.Follow;
import com.project.teachmteachybackend.enums.FollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsBySenderIdAndReceiverId(Long id, Long id1);

    Follow findBySenderIdAndReceiverId(Long friendId, Long userId);

    List<Follow> findByReceiverIdAndStatus(Long userId, FollowStatus followStatus);

    boolean existsBySenderIdAndReceiverIdAndStatus(Long id, Long id1, FollowStatus followStatus);
}
