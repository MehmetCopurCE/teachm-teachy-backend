package com.project.teachmteachybackend.repositories;

import com.project.teachmteachybackend.entities.FriendRequest;
import com.project.teachmteachybackend.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
//    List<FriendRequest> findBySenderId(Long senderId);
    List<FriendRequest> findByReceiverIdAndStatus(Long receiverId, FriendshipStatus pending);

    List<FriendRequest> findBySenderIdAndReceiverId(Long id, Long id1);


}
