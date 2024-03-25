package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.enums.AccountPrivacy;
import com.project.teachmteachybackend.enums.Role;
import com.project.teachmteachybackend.entities.User;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        user.setAccountPrivacy(createRequest.getAccountPrivacy());
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
            existingUser.setAccountPrivacy(createRequest.getAccountPrivacy());
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
}
