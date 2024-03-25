package com.project.teachmteachybackend.repositories;

import com.project.teachmteachybackend.entities.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    //List<Friendship> findByUserId1OrUserId2(Long userId1, Long userId2);
}
