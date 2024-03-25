package com.project.teachmteachybackend.controllers;

import com.project.teachmteachybackend.dto.friend.request.FriendshipRequest;
import com.project.teachmteachybackend.services.FriendshipService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> createFriendRequest(@RequestBody @Valid FriendshipRequest friendRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return friendshipService.sendFriendRequest(friendRequest);
    }

//    @GetMapping("/request/{userId}")
//    public ResponseEntity<List<FriendRequest>> getFriendRequests(@PathVariable Long userId){
//        List<FriendRequest> requestList = friendshipService.getFriendRequestBuId(userId);
//        return ResponseEntity.ok(requestList);
//    }
//
//    @GetMapping("friends/{userId}")
//    public ResponseEntity<List<FriendResponse>> getFriends(@PathVariable Long userId){
//        List<FriendResponse> friendResponseList = friendshipService.getFriendsById(userId);
//        return ResponseEntity.ok(friendResponseList);
//    }

}
