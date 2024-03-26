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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ./users
 * ./users/{userId}
 */

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ------------- User Management -------------
     */
    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest createRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        User savedUser = userService.saveUser(createRequest);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return userService.findUserById(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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
                    Map.of("success", true, "message", "Arkadaşlık isteği gönderildi.")
            );
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(
                    Map.of("success", false, "message", "Kullanıcı bulunamadı.")
            );
        } catch (FriendRequestExistsException e) {
            return ResponseEntity.status(409).body(
                    Map.of("success", false, "message", "Zaten bir arkadaşlık isteği mevcut veya kullanıcılar zaten arkadaş.")
            );
        } catch (FriendRequestException e) {
            return ResponseEntity.status(400).body(
                    Map.of("success", false, "message", "Kullanıcı zaten takip ediliyor")
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    Map.of("success", false, "message", "Arkadaşlık isteği gönderilemedi.")
            );
        }
    }

    @GetMapping("/{userId}/friend-requests")
    public ResponseEntity<List<FollowResponse>> getFriendRequests(@PathVariable Long userId) {
        List<FollowResponse> requestsList = userService.getFriendRequests(userId);
        return ResponseEntity.ok(requestsList);
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
    public ResponseEntity<List<FriendResponse>> getFriends(@PathVariable Long userId) {
        List<FriendResponse> friendList = userService.getFriendsById(userId);
//        List<Long> friendIds = new ArrayList<>();
//        for (Follow follow : followList) {
//            if (follow.getSenderId().equals(userId)) {
//                friendIds.add(follow.getReceiverId());
//            } else {
//                friendIds.add(follow.getSenderId());
//            }
//        }
//        List<User> friends = userRepository.findAllById(friendIds);
//        return ResponseEntity.ok(friends);
        return ResponseEntity.ok(friendList);

    }


}
