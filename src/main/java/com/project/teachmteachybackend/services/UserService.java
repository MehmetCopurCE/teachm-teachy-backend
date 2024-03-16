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
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    public User saveUser(UserCreateRequest createRequest) {
        User user = new User();
        user.setUsername(createRequest.getUsername());
        user.setPassword(createRequest.getPassword());
        user.setFirstName(createRequest.getFirstName());
        user.setLastName(createRequest.getLastName());
        user.setEmail(createRequest.getEmail());
        user.setUserStatistic(0.0);
        Role myRole = roleService.getRoleByName("ROLE_USER");
        if(myRole == null){
            Role userRole = new Role("ROLE_USER");
            myRole = roleService.saveRole(userRole);
        }
        user.setRoles(new HashSet<>(Collections.singleton(myRole)));
        user.setCreated_at(new Date());
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
            existingUser.setPassword(createRequest.getPassword());
            existingUser.setFirstName(createRequest.getFirstName());
            existingUser.setLastName(createRequest.getLastName());
            existingUser.setEmail(createRequest.getEmail());
            existingUser.setRoles(existingUser.getRoles());
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
}
