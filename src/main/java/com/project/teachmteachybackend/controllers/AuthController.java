package com.project.teachmteachybackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String login(){
        return "Login end point";
    }
    @GetMapping("/register")
    public String register(){
        return "Register end point";
    }
}
