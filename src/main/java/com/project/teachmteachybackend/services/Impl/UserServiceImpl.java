package com.project.teachmteachybackend.services.Impl;

import com.project.teachmteachybackend.dto.follow.response.FollowResponse;
import com.project.teachmteachybackend.dto.friend.response.FriendResponse;
import com.project.teachmteachybackend.dto.friend.response.FriendshipResponse;
import com.project.teachmteachybackend.entities.Follow;
import com.project.teachmteachybackend.enums.AccountPrivacy;
import com.project.teachmteachybackend.enums.FollowStatus;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.exceptions.*;
import com.project.teachmteachybackend.repositories.FollowRepository;
import com.project.teachmteachybackend.repositories.UserRepository;
import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import com.project.teachmteachybackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followRepository = followRepository;
    }
    /** ------------- User Management ------------- */

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
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

    @Override
    public boolean deleteUser(Long userId) {
        return userRepository.findById(userId).map(user ->{
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


    /** ------------- Friendship Management ------------- */
    @Override
    public FriendshipResponse sendFriendRequest(Long userId, Long friendId) throws UserNotFoundException, FriendRequestExistsException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı with ID: "+ userId ));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı with ID: "+ friendId ));

        if (userId.equals(friendId)){
            throw new BaseErrorException("FriendshipError", "User cannot send friend request him/herself!", HttpStatus.BAD_REQUEST);
        }
        if(isFriend(user, friend)){
            throw new FriendRequestException("Friend already followed");
        }

        FriendshipResponse response = new FriendshipResponse();
        // Hesap gizliyse, istek gönder
        if (friend.getAccountType() == AccountPrivacy.PRIVATE) {
            if (isFriendRequestExists(user, friend)) {
                Follow existingFollow = followRepository.findBySenderIdAndReceiverId(userId, friendId);
                if(existingFollow.getStatus() == FollowStatus.REJECTED){
                    response = reSendRequest(existingFollow, friend);

                }else{
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    response.setMessage(friend.getFirstName()+ " "+ friend.getLastName() +" için zaten bir arkadaşlık isteği mevcut.");
                }
            }else{
                response = sendRequest(user, friend);
            }
        } else {
            response =  addFriend(user, friend);
        }
        return  response;
    }

    private boolean isFriendRequestExists(User user, User friend) {
        // TakipIstekleri tablosunda kayıt olup olmadığını kontrol edin
        return followRepository.existsBySenderIdAndReceiverIdAndStatus(user.getId(), friend.getId(), FollowStatus.PENDING) ||
                followRepository.existsBySenderIdAndReceiverIdAndStatus(user.getId(), friend.getId(), FollowStatus.REJECTED);
    }

    private boolean isFriend(User user, User friend) {
        // Takip eden ve takip edilen kullanıcıların ID'lerini kullanarak Follow tablosunda kayıt olup olmadığını kontrol edin
        return followRepository.existsBySenderIdAndReceiverIdAndStatus(user.getId(), friend.getId(), FollowStatus.ACCEPTED);
    }

    private FriendshipResponse sendRequest(User user, User friend) {
        FriendshipResponse response = new FriendshipResponse();
        try {
            Follow followRequest = new Follow();
            followRequest.setSenderId(user.getId());
            followRequest.setReceiverId(friend.getId());
            followRequest.setCreatedAt(LocalDateTime.now());
            followRequest.setSenderName(user.getUsername());
            followRequest.setStatus(FollowStatus.PENDING);
            followRepository.save(followRequest);

            response.setStatus(HttpStatus.OK);
            response.setMessage("Friend request sent to " + friend.getFirstName()+ " " +friend.getLastName()+ " successfully!");

        }catch (Exception e){
            System.out.println("Friend request could not send, Error : " + e);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Friend request could not send to " + friend.getFirstName()+ " " +friend.getLastName()+ " . Error: "+ e.getMessage());
        }
        return response;
    }

    private FriendshipResponse reSendRequest(Follow follow, User friend) {
        FriendshipResponse response = new FriendshipResponse();
        try {
            follow.setStatus(FollowStatus.PENDING);
            followRepository.save(follow);

            response.setStatus(HttpStatus.OK);
            response.setMessage("Friend request re-sent to "+ friend.getFirstName()+ " "+ friend.getLastName() +" successfully!");

        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Friend request could not send to " + friend.getFirstName()+ " " +friend.getLastName()+ " . Error: "+ e.getMessage());
        }
        return response;
    }

    private FriendshipResponse addFriend(User user, User friend) {
        FriendshipResponse response = new FriendshipResponse();
        try {
            Follow followRequest = new Follow();
            followRequest.setSenderId(user.getId());
            followRequest.setReceiverId(friend.getId());
            followRequest.setCreatedAt(LocalDateTime.now());
            followRequest.setSenderName(user.getUsername());
            followRequest.setStatus(FollowStatus.ACCEPTED);
            followRepository.save(followRequest);

            response.setStatus(HttpStatus.OK);
            response.setMessage(friend.getFirstName()+ " " +friend.getLastName()+ " added as friend");
        }catch (Exception e){
            System.out.println("Friend could not added as friend, Error : " + e);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(friend.getFirstName()+ " " +friend.getLastName()+ " cannot added as friend. Error: "+ e.getMessage());
        }

        return response;
    }

    @Override
    public List<FollowResponse> getFriendRequests(Long userId) {
        // Kullanıcının varlığını kontrol edin
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User cannot found with userId: " + userId); // Özel bir istisna fırlatarak hatayı anlamlandırın
        }

        List<Follow> requestList = followRepository.findByReceiverIdAndStatus(userId, FollowStatus.PENDING);
        return requestList.stream().map(FollowResponse::new).collect(Collectors.toList());
    }

    @Override
    public FriendshipResponse acceptFriendRequest(Long userId, Long friendId) throws UserNotFoundException, FriendRequestNotFoundException{
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı with ID: "+ userId ));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı with ID: "+ friendId ));

        Follow follow = followRepository.findBySenderIdAndReceiverId(friendId, userId);
        Follow follow2 = followRepository.findBySenderIdAndReceiverId(userId, friendId);


        if(follow == null){
            throw new FriendRequestNotFoundException("Arkadaşlık isteği bulunamadı.");
        }

        if(isFriend(user, friend)){
            throw new FriendRequestException("They are friend");
        }

        follow.setStatus(FollowStatus.ACCEPTED);
        followRepository.save(follow);

        if(follow2 != null){
            follow2.setStatus(FollowStatus.ACCEPTED);
            followRepository.save(follow2);
        }else{
            addFriend(user,friend);
        }

        FriendshipResponse response = new FriendshipResponse();
        response.setStatus(HttpStatus.OK);
        response.setMessage(friend.getFirstName() + " " + friend.getLastName()+ "'in arkadaşlık isteği kabul edildi");

        return response;
    }

    @Override
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

    @Override
    public List<FriendResponse> getFriendsByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<Follow> acceptedFollowsSent = followRepository.findBySenderIdAndStatus(userId, FollowStatus.ACCEPTED);
        List<Follow> acceptedFollowsReceived = followRepository.findByReceiverIdAndStatus(userId, FollowStatus.ACCEPTED);

        Set<Long> friendIds = new HashSet<>();
        for (Follow follow : acceptedFollowsSent) {
            friendIds.add(follow.getReceiverId());
        }
        for (Follow follow : acceptedFollowsReceived) {
            friendIds.add(follow.getSenderId());
        }

        // Retrieve friend user objects
        return userRepository.findAllById(friendIds).stream().map(FriendResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<FollowResponse> getRejectedRequestsByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User cannot found with userId: " + userId); // Özel bir istisna fırlatarak hatayı anlamlandırın
        }

        List<Follow> requestList = followRepository.findByReceiverIdAndStatus(userId, FollowStatus.REJECTED);
        return requestList.stream().map(FollowResponse::new).collect(Collectors.toList());

    }

    @Override
    public FriendshipResponse unfollowFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı with ID: "+ userId ));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı with ID: "+ friendId ));
        FriendshipResponse response = new FriendshipResponse();

        if (isFriend(user, friend) || isFriend(friend,user)) {
            Follow follow = followRepository.findBySenderIdAndReceiverId(user.getId(), friend.getId());
            Follow follow2 = followRepository.findBySenderIdAndReceiverId(friend.getId(), user.getId());
            follow.setStatus(FollowStatus.REJECTED);
            followRepository.save(follow);

            if(follow2 != null){
                follow2.setStatus(FollowStatus.REJECTED);
                followRepository.save(follow2);
            }

            response.setMessage(friend.getFirstName() + " " + friend.getLastName() + " is unfollowed");
            response.setStatus(HttpStatus.OK);
        }else{
            response.setMessage(friend.getFirstName() + " " + friend.getLastName() + " is not unfollowed");
            response.setStatus(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
