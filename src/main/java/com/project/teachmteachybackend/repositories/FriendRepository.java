package com.project.teachmteachybackend.repositories;

import com.project.teachmteachybackend.entities.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
