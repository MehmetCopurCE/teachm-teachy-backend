package com.project.teachmteachybackend.controllers;

import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.request.UserCreateRequest;
import com.project.teachmteachybackend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 ./users
 ./users/{userId}
 */

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest createRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        User savedUser =  userService.saveUser(createRequest);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return  userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId){
        return userService.findUserById(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody @Valid UserCreateRequest createRequest){
        return userService.updateUser(userId, createRequest).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId){
        if(userService.deleteUser(userId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
