package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.entities.Role;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.repositories.RoleRepository;
import com.project.teachmteachybackend.repositories.UserRepository;
import com.project.teachmteachybackend.request.UserCreateRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User saveUser(UserCreateRequest createRequest) {
        User user = new User();
        user.setUsername(createRequest.getUsername());
        user.setPassword(createRequest.getPassword());
        user.setFirstName(createRequest.getFirstName());
        user.setLastName(createRequest.getLastName());
        user.setEmail(createRequest.getEmail());
        user.setUserStatistic(0.0);
        Role defaultRole = roleRepository.findByRoleName("ROLE_USER");
        user.setRoles(new HashSet<>(Collections.singleton(defaultRole)));
        user.setCreated_at(new Date());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> updateUser(Long userId, User user) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setRoles(user.getRoles());
            existingUser.setUserStatistic(user.getUserStatistic());
            return userRepository.save(existingUser);
        });
    }

    public boolean deleteUser(Long userId) {
        return userRepository.findById(userId).map(user ->{
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }
}
