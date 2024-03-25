package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import com.project.teachmteachybackend.dto.user.request.UserLoginRequest;
import com.project.teachmteachybackend.dto.user.response.AuthResponse;
import com.project.teachmteachybackend.enums.Role;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.exceptions.InvalidCredentialsException;
import com.project.teachmteachybackend.exceptions.UserNotFoundException;
import com.project.teachmteachybackend.repositories.UserRepository;
import com.project.teachmteachybackend.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public AuthResponse login(UserLoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if(user.isEmpty())
            throw new UserNotFoundException("User not found with username:" + request.getUsername());

        if(!passwordEncoder.matches(request.getPassword(),user.get().getPassword()))
            throw new InvalidCredentialsException("The username or password you entered is incorrect. Please try again.");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateToken(request.getUsername());

        AuthResponse response = new AuthResponse();
        response.setUserId(user.get().getId());
        response.setAccessToken("Bearer " + jwtToken);
        response.setMessage("Successfully Login");
        return response;
    }

    public ResponseEntity<AuthResponse> register(UserCreateRequest request) {
        AuthResponse authResponse = new AuthResponse();
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            authResponse.setMessage("Username already in use.");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setQuestion(request.getQuestion());
        user.setAnswer(request.getAnswer());
        user.setRole(Role.USER);
        user.setAccountPrivacy(request.getAccountPrivacy());
        user.setRegistrationTime(LocalDateTime.now());
        user.setUserStatistic(0.0);

        userRepository.save(user);

        authResponse.setMessage("Successfully registered!");
        authResponse.setUserId(user.getId());
        authResponse.setAccessToken("");
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    };
}
