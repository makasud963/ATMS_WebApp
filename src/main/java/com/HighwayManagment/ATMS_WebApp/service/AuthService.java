package com.HighwayManagment.ATMS_WebApp.service;

import com.HighwayManagment.ATMS_WebApp.dto.AuthRequest;
import com.HighwayManagment.ATMS_WebApp.dto.LoginRequest;
import com.HighwayManagment.ATMS_WebApp.dto.LoginResponseDTO;
import com.HighwayManagment.ATMS_WebApp.dto.UserResponseDTO;
import com.HighwayManagment.ATMS_WebApp.entity.UserMaster;
import com.HighwayManagment.ATMS_WebApp.entity.UserSession;
import com.HighwayManagment.ATMS_WebApp.repository.UserRepository;
import com.HighwayManagment.ATMS_WebApp.security.JwtService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserSessionService userSessionService;

    @Autowired
    private MessageSource messageSource;

    public String register(AuthRequest request) {

        if (request.getUserName()== null && request.getUserEmail()== null) {
            return messageSource.getMessage(
                    "error.user.notnull",
                    null,
                    Locale.getDefault());
        }

            // Check if the modified side name conflicts with another active side record
        if (request.getUserName()!= null) {

            boolean exists = userRepository.existsByUserName(request.getUserName());

            if (exists) {
                return messageSource.getMessage(
                                "error.user.exists",
                                null,
                                Locale.getDefault());
            }
        }
        // Check if the modified side name conflicts with another active side record
        if (request.getUserEmail()!= null) {

            boolean exists = userRepository.existsByEmail(request.getUserEmail());

            if (exists) {
                return messageSource.getMessage(
                                "error.userEmail.exists",
                                null,
                                Locale.getDefault());
            }
        }

        UserMaster userMaster = UserMaster.builder()
                .userName(request.getUserName())
                .email(request.getUserEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
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

    public ResponseEntity<String> deleteUser(Long Id) {

        UserMaster user = userRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage(
                                "error.user.notfound",
                                null,
                                Locale.getDefault()
                        )
                ));
        user.setActive(false);
        userRepository.save(user);

        return ResponseEntity.ok(
                messageSource.getMessage(
                        "success.user.deleted",
                        null,
                        Locale.getDefault()
                )
        );
    }

    public List<UserResponseDTO> getAllActiveUsers() {

        List<UserMaster> users = userRepository.findByActiveTrue();

        List<UserResponseDTO> dtoList = new ArrayList<>();

        for (UserMaster usr : users) {

            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(usr.getUserId());
            dto.setName(usr.getUserName());
            dto.setEmail(usr.getEmail());
            dto.setPassword(usr.getPassword());
            dto.setRole(usr.getRole());
            dto.setActive(usr.isActive());

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional
    public ResponseEntity<String> updateUser(Long userId, UserResponseDTO dto) {

        UserMaster user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage(
                                "error.user.notfound",
                                null,
                                Locale.getDefault()
                        )
                ));

        // Check username uniqueness excluding current user
        if (userRepository.existsByUserNameIgnoreCaseAndUserIdNot(dto.getName(), userId)) {

            return ResponseEntity.badRequest().body(
                    messageSource.getMessage(
                            "error.user.exists",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        // Check email uniqueness excluding current user
        if (userRepository.existsByEmailIgnoreCaseAndUserIdNot(
                dto.getEmail(), userId)) {

            return ResponseEntity.badRequest().body(
                    messageSource.getMessage(
                            "error.userEmail.exists",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        user.setUserName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setActive(dto.isActive());

        // Update password only if provided
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            if (!user.getPassword().equalsIgnoreCase(dto.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }
        userRepository.save(user);

        return ResponseEntity.ok(
                messageSource.getMessage(
                        "success.user.updated",
                        null,
                        Locale.getDefault()
                )
        );
    }

//    public ResponseEntity<String> updateUser(Long userId, UserResponseDTO dto) {
//
//        UserMaster user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException(
//                        messageSource.getMessage(
//                                "error.user.notfound",
//                                null,
//                                Locale.getDefault()
//                        )
//                ));
//
//        // Check if the modified side name conflicts with another active side record
//        if (!user.getUserName().equalsIgnoreCase(dto.getName())) {
//
//            boolean exists = userRepository.existsByUserName(dto.getName());
//
//            if (exists) {
//                return ResponseEntity.badRequest().body(
//                        messageSource.getMessage(
//                                "error.user.exists",
//                                null,
//                                Locale.getDefault()
//                        )
//                );
//            }
//        }
//        // Check if the modified side name conflicts with another active side record
//        if (!user.getEmail().equalsIgnoreCase(dto.getEmail())) {
//
//            boolean exists = userRepository.existsByEmail(dto.getEmail());
//
//            if (exists) {
//                return ResponseEntity.badRequest().body(
//                        messageSource.getMessage(
//                                "error.userEmail.exists",
//                                null,
//                                Locale.getDefault()
//                        )
//                );
//            }
//        }
//
//        user.setUserName(dto.getName());
//        user.setEmail(dto.getEmail());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user.setRole(dto.getRole());
//        user.setActive(dto.isActive());
//        userRepository.save(user);
//
//        return ResponseEntity.ok(
//                messageSource.getMessage(
//                        "success.user.updated",
//                        null,
//                        Locale.getDefault()
//                )
//        );
//    }
}