package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.follow.response.FollowResponse;
import com.project.teachmteachybackend.dto.friend.response.FriendResponse;
import com.project.teachmteachybackend.dto.friend.response.FriendshipResponse;
import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import com.project.teachmteachybackend.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /** ------------- User Management ------------- */
    List<User> getAllUsers();

    Optional<User> findUserById(Long userId);

    Optional<User> updateUser(Long userId, UserCreateRequest createRequest);

    boolean deleteUser(Long userId);

    User getUserById(Long userId);

    User getUserByUsername(String username);



    /** ------------- Friendship Management ------------- */

    FriendshipResponse sendFriendRequest(Long userId, Long friendId);

    List<FollowResponse> getFriendRequests(Long userId);

    FriendshipResponse acceptFriendRequest(Long userId, Long friendId);

    void rejectFriendRequest(Long userId, Long friendId);

    List<FriendResponse> getFriendsByUserId(Long userId);

    List<FollowResponse> getRejectedRequestsByUserId(Long userId);
}
