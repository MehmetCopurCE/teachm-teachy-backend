package com.project.teachmteachybackend.services;

import com.project.teachmteachybackend.dto.refresh.request.RefreshRequest;
import com.project.teachmteachybackend.dto.refresh.response.RefreshResponse;
import com.project.teachmteachybackend.dto.user.request.UserCreateRequest;
import com.project.teachmteachybackend.dto.user.request.UserLoginRequest;
import com.project.teachmteachybackend.dto.user.response.AuthResponse;
import com.project.teachmteachybackend.entities.RefreshToken;
import com.project.teachmteachybackend.enums.Role;
import com.project.teachmteachybackend.entities.User;
import com.project.teachmteachybackend.exceptions.ExistEmailException;
import com.project.teachmteachybackend.exceptions.InvalidCredentialsException;
import com.project.teachmteachybackend.exceptions.UserNotFoundException;
import com.project.teachmteachybackend.exceptions.UsernameInUseException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, UserRepository userRepository, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthResponse login(UserLoginRequest request) throws UserNotFoundException, InvalidCredentialsException{
        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if(user.isEmpty())
            throw new UserNotFoundException("User not found with username : " + request.getUsername());

        if(!passwordEncoder.matches(request.getPassword(),user.get().getPassword()))
            throw new InvalidCredentialsException("The username or password you entered is incorrect. Please try again.");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateToken(request.getUsername());

        AuthResponse response = new AuthResponse();
        response.setStatus(String.valueOf(HttpStatus.OK.value()));
        response.setUserId(user.get().getId());
        response.setAccessToken("Bearer " + jwtToken);
        response.setRefreshToken(refreshTokenService.createRefreshToken(user));
        response.setMessage("Successfully Login");
        return response;
    }

    public ResponseEntity<AuthResponse> register(UserCreateRequest request) throws UsernameInUseException, ExistEmailException {
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UsernameInUseException("Username is in use : " + request.getUsername());
        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new ExistEmailException("Email is in use : " + request.getEmail());
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
        user.setAccountType(request.getAccountPrivacy());
        user.setRegistrationTime(LocalDateTime.now());
        user.setUserStatistic(0.0);
        userRepository.save(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateToken(auth.getName());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Successfully registered!");
        authResponse.setStatus(String.valueOf(HttpStatus.CREATED.value()));
        authResponse.setUserId(user.getId());
        authResponse.setAccessToken("Bearer " + jwtToken);
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(Optional.of(user)));

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    };

    public ResponseEntity<RefreshResponse> refresh(RefreshRequest refreshRequest){
        RefreshResponse response = new RefreshResponse();
        RefreshToken token = refreshTokenService.getByUser(refreshRequest.getUserId());
        if(token.getToken().equals(refreshRequest.getRefreshToken()) &&
                !refreshTokenService.isRefreshExpired(token)) {

            User user = token.getUser();
            String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());
            response.setMessage("Token successfully refreshed.");
            response.setAccessToken("Bearer " + jwtToken);
            response.setUserId(user.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {
            response.setMessage("refresh token is not valid.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

        }
    }
}
