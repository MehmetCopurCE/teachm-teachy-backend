package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.friend.request.FriendshipRequest;
import com.project.teachmteachybackend.entities.FriendRequest;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.enums.FriendshipStatus;
import com.project.teachmteachybackend.repositories.FriendRequestRepository;
import com.project.teachmteachybackend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendshipService {
    private final FriendRequestRepository requestRepository;
    private final UserRepository userRepository;

    public FriendshipService(FriendRequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> sendFriendRequest(FriendshipRequest friendRequest) {
        User sender = userRepository.findById(friendRequest.getSenderId()).orElse(null);
        User receiver = userRepository.findById(friendRequest.getReceiverId()).orElse(null);

        if(sender == null || receiver == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender or receiver cannot found");
        }

        List<FriendRequest> existingRequestList = requestRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());

        if(!existingRequestList.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is already an pending request");
        }

            FriendRequest request = new FriendRequest();
            request.setSender(sender);
            request.setReceiver(receiver);
            request.setStatus(FriendshipStatus.PENDING);
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            requestRepository.save(request);

        return ResponseEntity.ok("Friend request sent successfully!");
    }

//    public List<FriendRequest> getFriendRequestBuId(Long userId) {
//        return requestRepository.findByReceiverIdAndStatus(userId, FriendshipStatus.PENDING);
//    }
//
//    public List<FriendResponse> getFriendsById(Long userId) {
//        List<Friendship> friendshipList = friendshipRepository.findByUserId1OrUserId2(userId,userId);
//        List<FriendResponse> friendResponseList = new ArrayList<>();
//        for(Friendship friendship : friendshipList){
//            User friend = friendship.getUser1().getId().equals(userId) ? friendship.getUser1() : friendship.getUser2();
//            FriendResponse friendResponse = new FriendResponse();
//            friendResponse.setUserId(friend.getId());
//            friendResponse.setUsername(friend.getUsername());
//            friendResponse.setFirstName(friend.getFirstName());
//            friendResponse.setLastName(friend.getLastName());
//            friendResponseList.add(friendResponse);
//        }
//        return friendResponseList;
//    }
}
