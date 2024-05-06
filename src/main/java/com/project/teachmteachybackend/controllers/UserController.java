package com.project.teachmteachybackend.controllers;

import com.project.teachmteachybackend.dto.follow.response.FollowResponse;
import com.project.teachmteachybackend.dto.friend.response.FriendResponse;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import com.project.teachmteachybackend.exceptions.FriendRequestException;
import com.project.teachmteachybackend.exceptions.FriendRequestExistsException;
import com.project.teachmteachybackend.exceptions.FriendRequestNotFoundException;
import com.project.teachmteachybackend.exceptions.UserNotFoundException;
import com.project.teachmteachybackend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ./api/users
 * ./api/users/{userId}
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ------------- User Management -------------
     */
//    @PostMapping()
//    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest createRequest, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
//        }
//        User savedUser = userService.saveUser(createRequest);
//        return ResponseEntity.ok(savedUser);
//    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        return userService.findUserById(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı, userId: " + userId));
    }



    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody @Valid UserCreateRequest createRequest) {
        return userService.updateUser(userId, createRequest).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (userService.deleteUser(userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * ------------- Friendship Management -------------
     */

    @PostMapping("/{userId}/send-friend-request")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long userId, @RequestParam Long friendId) {
        try {
            userService.sendFriendRequest(userId, friendId);
            return ResponseEntity.ok().body(
                    Map.of("success", true, "message", "Friend request sent successfully.")
            );
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(
                    Map.of("success", false, "message", "User not found.")
            );
        } catch (FriendRequestExistsException e) {
            return ResponseEntity.status(409).body(
                    Map.of("success", false, "message", "There already exist a friend request or they are already friends.")
            );
        } catch (FriendRequestException e) {
            return ResponseEntity.status(400).body(
                    Map.of("success", false, "message", "User already followed.")
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("success", false, "message", "Friend request could not send.")
            );
        }
    }

    @GetMapping("/{userId}/friend-requests")
    public ResponseEntity<?> getFriendRequests(@PathVariable Long userId) {
        try {
            List<FollowResponse> requestsList = userService.getFriendRequests(userId);
            return ResponseEntity.ok(requestsList);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User not found"));
        }
    }

    @PostMapping("/{userId}/accept-friend-request")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long userId, @RequestParam Long senderId) {
        try {
            userService.acceptFriendRequest(userId, senderId);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Arkadaşlık isteği kabul edildi."));
        } catch (FriendRequestNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Arkadaşlık isteği bulunamadı."));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Sender or Receiver user cannot found."));
        }catch (FriendRequestException e) {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", "They are already friends."));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", "Arkadaşlık isteği kabul edilemedi."));
        }
    }

    @PostMapping("/{userId}/reject-friend-request")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable Long userId, @RequestParam Long senderId) {
        try {
            userService.rejectFriendRequest(userId, senderId);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Friend request rejected"));
        } catch (FriendRequestNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Friend request could not found."));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Sender or Receiver user cannot found."));
        }catch (FriendRequestException e) {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", "They are already friends."));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", "Friend request could not rejected."));
        }
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<?> getFriends(@PathVariable Long userId) {
        try {
            List<FriendResponse> friendList = userService.getFriendsById(userId);
            return ResponseEntity.ok(friendList);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "User not found"));
        }
    }

    @GetMapping("/{userId}/rejected-requests")
    public  ResponseEntity<?> getRejectedRequest(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getRejectedRequestsById(userId));
    }


}
