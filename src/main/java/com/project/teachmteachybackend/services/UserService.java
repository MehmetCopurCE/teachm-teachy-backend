package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.entities.Follow;
import com.project.teachmteachybackend.entities.Friend;
import com.project.teachmteachybackend.enums.AccountPrivacy;
import com.project.teachmteachybackend.enums.Role;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.exceptions.FriendRequestExistsException;
import com.project.teachmteachybackend.exceptions.UserNotFoundException;
import com.project.teachmteachybackend.repositories.FollowRepository;
import com.project.teachmteachybackend.repositories.FriendRepository;
import com.project.teachmteachybackend.repositories.UserRepository;
import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final FriendRepository friendRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FollowRepository followRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followRepository = followRepository;
        this.friendRepository = friendRepository;
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

        // Hesap gizliyse, istek gönder
        if (friend.getAccountType() == AccountPrivacy.PRIVATE) {
            if (isFriendRequestExists(user, friend)) {
                throw new FriendRequestExistsException("Zaten bir arkadaşlık isteği mevcut.");
            }
            sendRequest(user, friend);
        } else {
            addFriend(user, friend);
        }
    }

    private boolean isFriendRequestExists(User user, User friend) {
        // TakipIstekleri tablosunda kayıt olup olmadığını kontrol edin
        return followRepository.existsBySenderIdAndReceiverId(user.getId(), friend.getId());
    }

    private void sendRequest(User user, User friend) {
        try {
            Follow followRequest = new Follow();
            followRequest.setSenderId(user.getId());
            followRequest.setReceiverId(friend.getId());
            followRequest.setCreatedAt(LocalDateTime.now());
            followRepository.save(followRequest);
        }catch (Exception e){
            System.out.println("Friend request could not send, Error : " + e);
        }
    }

    private void addFriend(User user, User friend) {
        try {
            Friend toSave = new Friend();
            toSave.setUserId(user.getId());
            toSave.setFriendId(friend.getId());
            toSave.setCreatedAt(LocalDateTime.now());
            friendRepository.save(toSave);
        }catch (Exception e){
            System.out.println("Friend could not added as friend, Error : " + e);
        }
    }
}
