package com.project.teachmteachybackend.services.Impl;

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
import com.project.teachmteachybackend.services.AuthService;
import com.project.teachmteachybackend.services.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, UserRepository userRepository, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    private Random random = new Random();
    private List<String> avatarUrls = Arrays.asList(
            "https://img.freepik.com/free-psd/3d-render-avatar-character_23-2150611728.jpg?t=st=1716756430~exp=1716760030~hmac=d352a0d17e5ba5a2667baf922574182604559437def8925070ee18a52afc159f&w=740",
            "https://img.freepik.com/free-psd/3d-render-avatar-character_23-2150611768.jpg?t=st=1716756451~exp=1716760051~hmac=cd8a77bb2f15817915fb5822814387ff67703d364538595a7f898b13c2080760&w=740",
            "https://img.freepik.com/free-psd/3d-render-avatar-character_23-2150611765.jpg?t=st=1716756472~exp=1716760072~hmac=078e0328badcd98b675eb97db6fb41f34a93c658b7df42b71e5b9a9f57836483&w=740",
            "https://img.freepik.com/free-psd/3d-render-avatar-character_23-2150611731.jpg?t=st=1716756494~exp=1716760094~hmac=899d028d8089fd866738dd76c59adef8dab146305eda59a7fa01937d18041168&w=740",
            "https://img.freepik.com/free-psd/3d-illustration-person-with-glasses_23-2149436185.jpg?t=st=1716756513~exp=1716760113~hmac=73f26cb139c9b885e119a8cfc8246e2806ae518d60ebd994a8bfaefe205ef0e6&w=740",
            "https://img.freepik.com/free-psd/3d-render-avatar-character_23-2150611710.jpg?t=st=1716756580~exp=1716760180~hmac=916b03e3dca19bce449cf0593010a1fdeca07351335f70cfff9ebc2c91b5a822&w=740",
            "https://img.freepik.com/free-psd/3d-rendering-avatar_23-2150833536.jpg?t=st=1716756630~exp=1716760230~hmac=a75fefa626b3bd564f4937374cc794115f2cda16faf0b6dab8859ef59a331d85&w=740",
            "https://img.freepik.com/free-psd/3d-rendering-avatar_23-2150833548.jpg?t=st=1716756680~exp=1716760280~hmac=13e021c6f447cd85f7d6155d3ea614969b73aeea8a8c57edbe67da70c9f99a76&w=740",
            "https://img.freepik.com/free-psd/3d-illustration-person-tank-top_23-2149436199.jpg?t=st=1716756721~exp=1716760321~hmac=8eef14343a43d507b3bfccbba2cb0fb3fa64336395806afbd2b12f2d612786a2&w=740",
            "https://img.freepik.com/free-psd/3d-illustration-person-with-glasses_23-2149436185.jpg?t=st=1716756513~exp=1716760113~hmac=73f26cb139c9b885e119a8cfc8246e2806ae518d60ebd994a8bfaefe205ef0e6&w=740",
            "https://img.freepik.com/free-psd/3d-illustration-person-with-sunglasses_23-2149436180.jpg?t=st=1716757233~exp=1716760833~hmac=4bfa8f8a96f4610dbb2dfab9866964687642659632c79903e43f6e33bf94d03f&w=740"
    );

    @Override
    public AuthResponse login(UserLoginRequest request) throws UserNotFoundException, InvalidCredentialsException {

        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if (user.isEmpty())
            throw new UserNotFoundException("User not found with username : " + request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
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

    @Override
    public ResponseEntity<AuthResponse> register(UserCreateRequest request) throws UsernameInUseException, ExistEmailException {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameInUseException("Username is in use : " + request.getUsername());
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
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
        String randomAvatarUrl = avatarUrls.get(random.nextInt(avatarUrls.size()));
        user.setAvatarUrl(randomAvatarUrl);
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
    }

    private int getRandomAvatarId() {
        Random random = new Random();
        return random.nextInt(5) + 1;
    }

    ;

    @Override
    public ResponseEntity<RefreshResponse> refresh(RefreshRequest refreshRequest) {
        RefreshResponse response = new RefreshResponse();
        RefreshToken token = refreshTokenService.getByUser(refreshRequest.getUserId());
        if (token.getToken().equals(refreshRequest.getRefreshToken()) &&
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
