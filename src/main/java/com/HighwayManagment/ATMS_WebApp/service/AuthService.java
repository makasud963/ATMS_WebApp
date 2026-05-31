package com.HighwayManagment.ATMS_WebApp.service;

import com.HighwayManagment.ATMS_WebApp.dto.AuthRequest;
import com.HighwayManagment.ATMS_WebApp.dto.LoginRequest;
import com.HighwayManagment.ATMS_WebApp.dto.LoginResponseDTO;
import com.HighwayManagment.ATMS_WebApp.dto.UserResponseDTO;
import com.HighwayManagment.ATMS_WebApp.entity.UserMaster;
import com.HighwayManagment.ATMS_WebApp.entity.UserSession;
import com.HighwayManagment.ATMS_WebApp.repository.UserRepository;
import com.HighwayManagment.ATMS_WebApp.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserSessionService userSessionService;

    public String register(AuthRequest request) {

        UserMaster userMaster = UserMaster.builder()
                .userName(request.getUserName())
                .email(request.getUserEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(userMaster);

        return "User Registered Successfully";
    }

public LoginResponseDTO login(LoginRequest request) {

    UserMaster userMaster =
            userRepository.findByEmail(request.getUserEmail())
                    .orElseThrow(() ->
                            new RuntimeException("User Not Found"));

    if (!passwordEncoder.matches(
            request.getPassword(),
            userMaster.getPassword()
    )) {

        throw new RuntimeException("Invalid Password");
    }

    UserSession session =
            userSessionService.createSession(
                    userMaster.getEmail(),
                    request.getMachineId()
            );

    String accessToken =
            jwtService.generateToken(
                    userMaster.getUserId(),
                    userMaster.getEmail(),
                    session.getSessionId()
            );

    // Temporary refresh token
    // Later you can implement DB refresh token storage
    String refreshToken = java.util.UUID.randomUUID().toString();

    UserResponseDTO userResponse =  UserResponseDTO.builder()
                    .id(userMaster.getUserId())
                    .name(userMaster.getUserName())
                    .email(userMaster.getEmail())
                    .role(userMaster.getRole())
                    .build();

    return LoginResponseDTO.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(userResponse)
            .build();
}
    public void logout(String token) {
        Long sessionId = jwtService.extractSessionId(token);
        userSessionService.logoutSession(sessionId);
    }

    public String getEmailFromToken(String token) {
        return jwtService.extractEmail(token);
    }

    public Long getUserIdFromToken(String token) {
        return jwtService.extractUserId(token);
    }
}