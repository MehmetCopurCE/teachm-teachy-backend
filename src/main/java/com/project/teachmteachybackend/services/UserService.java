package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.follow.response.FollowResponse;
import com.project.teachmteachybackend.dto.friend.response.FriendResponse;
import com.project.teachmteachybackend.entities.Follow;
import com.project.teachmteachybackend.enums.AccountPrivacy;
import com.project.teachmteachybackend.enums.FollowStatus;
import com.project.teachmteachybackend.enums.Role;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.exceptions.FriendRequestException;
import com.project.teachmteachybackend.exceptions.FriendRequestExistsException;
import com.project.teachmteachybackend.exceptions.FriendRequestNotFoundException;
import com.project.teachmteachybackend.exceptions.UserNotFoundException;
import com.project.teachmteachybackend.repositories.FollowRepository;
import com.project.teachmteachybackend.repositories.UserRepository;
import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followRepository = followRepository;
    }
    /** ------------- User Management ------------- */
    public User saveUser(UserCreateRequest createRequest) {
        User user = new User();
        user.setUsername(createRequest.getUsername());
        user.setPassword(createRequest.getPassword());
        user.setFirstName(createRequest.getFirstName());
        user.setLastName(createRequest.getLastName());
        user.setEmail(createRequest.getEmail());
        user.setQuestion(createRequest.getQuestion());
        user.setAnswer(createRequest.getAnswer());
        user.setRole(Role.USER);
        user.setAccountType(createRequest.getAccountPrivacy());
        user.setUserStatistic(0.0);
        user.setRegistrationTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> updateUser(Long userId, UserCreateRequest createRequest) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setUsername(createRequest.getUsername());
            existingUser.setPassword(passwordEncoder.encode(createRequest.getPassword()));
            existingUser.setFirstName(createRequest.getFirstName());
            existingUser.setLastName(createRequest.getLastName());
            existingUser.setEmail(createRequest.getEmail());
            existingUser.setQuestion(createRequest.getQuestion());
            existingUser.setAnswer(createRequest.getAnswer());
            existingUser.setAccountType(createRequest.getAccountPrivacy());
            existingUser.setUserStatistic(existingUser.getUserStatistic());
            return userRepository.save(existingUser);
        });
    }

    public boolean deleteUser(Long userId) {
        return userRepository.findById(userId).map(user ->{
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }





    /** ------------- Friendship Management ------------- */
    public void sendFriendRequest(Long userId, Long friendId) throws UserNotFoundException, FriendRequestExistsException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı."));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı."));

        if(isFriend(user, friend)){
            throw new FriendRequestException("Friend already followed");
        }

        // Hesap gizliyse, istek gönder
        if (friend.getAccountType() == AccountPrivacy.PRIVATE) {
            if (isFriendRequestExists(user, friend)) {
                Follow existingFollow = followRepository.findBySenderIdAndReceiverId(userId, friendId);
                if(existingFollow.getStatus() == FollowStatus.REJECTED){
                    existingFollow.setStatus(FollowStatus.PENDING);
                    followRepository.save(existingFollow);
                }else{
                    throw new FriendRequestExistsException("Zaten bir arkadaşlık isteği mevcut.");
                }
            }else{
                sendRequest(user, friend);
            }
        } else {
            addFriend(user, friend);
        }
    }

    private boolean isFriendRequestExists(User user, User friend) {
        // TakipIstekleri tablosunda kayıt olup olmadığını kontrol edin
        return followRepository.existsBySenderIdAndReceiverId(user.getId(), friend.getId());
    }

    private boolean isFriend(User user, User friend) {
        // Takip eden ve takip edilen kullanıcıların ID'lerini kullanarak Follow tablosunda kayıt olup olmadığını kontrol edin
        return followRepository.existsBySenderIdAndReceiverIdAndStatus(user.getId(), friend.getId(), FollowStatus.ACCEPTED);
    }

    private void sendRequest(User user, User friend) {
        try {
            Follow followRequest = new Follow();
            followRequest.setSenderId(user.getId());
            followRequest.setReceiverId(friend.getId());
            followRequest.setCreatedAt(LocalDateTime.now());
            followRequest.setStatus(FollowStatus.PENDING);
            followRepository.save(followRequest);
        }catch (Exception e){
            System.out.println("Friend request could not send, Error : " + e);
        }
    }

    private void addFriend(User user, User friend) {
        try {
            Follow followRequest = new Follow();
            followRequest.setSenderId(user.getId());
            followRequest.setReceiverId(friend.getId());
            followRequest.setCreatedAt(LocalDateTime.now());
            followRequest.setStatus(FollowStatus.ACCEPTED);
            followRepository.save(followRequest);
        }catch (Exception e){
            System.out.println("Friend could not added as friend, Error : " + e);
        }
    }

    public List<FollowResponse> getFriendRequests(Long userId) {
        List<Follow> requestList = followRepository.findByReceiverIdAndStatus(userId, FollowStatus.PENDING);
        return requestList.stream().map(FollowResponse::new).collect(Collectors.toList());
    }

    public void acceptFriendRequest(Long userId, Long friendId) throws UserNotFoundException, FriendRequestNotFoundException{
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Kullanıcı bulunamadı."));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException("Friend Kullanıcı bulunamadı."));

        Follow follow = followRepository.findBySenderIdAndReceiverId(friendId, userId);

        if(follow == null){
            throw new FriendRequestNotFoundException("Arkadaşlık isteği bulunamadı.");
        }

        if(isFriend(user, friend)){
            throw new FriendRequestException("They are friend");
        }

        follow.setStatus(FollowStatus.ACCEPTED);
        followRepository.save(follow);
    }

    public void rejectFriendRequest(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Kullanıcı bulunamadı."));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException("Friend Kullanıcı bulunamadı."));

        Follow follow = followRepository.findBySenderIdAndReceiverId(friendId, userId);
        if(follow == null){
            throw new FriendRequestNotFoundException("Arkadaşlık isteği bulunamadı.");
        }

        if(isFriend(user, friend)){
            throw new FriendRequestException("They are friend");
        }

        follow.setStatus(FollowStatus.REJECTED);
        followRepository.save(follow);
    }

    public List<FriendResponse> getFriendsById(Long userId) {
        // Get all accepted follow requests for the user
        List<Follow> acceptedFollows = followRepository.findByReceiverIdAndStatus(userId, FollowStatus.ACCEPTED);

//        return  acceptedFollows.stream().map(follow -> {
//            Optional<User> user = userRepository.findById(follow.getId());
//            return new FriendResponse(user);
//        }).collect(Collectors.toList());

        // Extract friend user IDs
        List<Long> friendIds = new ArrayList<>();
        for (Follow follow : acceptedFollows) {
            friendIds.add(follow.getSenderId());
        }

        // Retrieve friend user objects
        return userRepository.findAllById(friendIds).stream().map(FriendResponse::new).collect(Collectors.toList());

    }
}
