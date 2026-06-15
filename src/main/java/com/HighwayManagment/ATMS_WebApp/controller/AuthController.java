package com.HighwayManagment.ATMS_WebApp.controller;

import com.HighwayManagment.ATMS_WebApp.controller.system.FirmMasterController;
import com.HighwayManagment.ATMS_WebApp.dto.*;
import com.HighwayManagment.ATMS_WebApp.dto.system.FirmMasterDTO;
import com.HighwayManagment.ATMS_WebApp.dto.system.SideMasterDTO;
import com.HighwayManagment.ATMS_WebApp.entity.UserMaster;
import com.HighwayManagment.ATMS_WebApp.repository.UserRepository;
import com.HighwayManagment.ATMS_WebApp.repository.UserSessionRepository;
import com.HighwayManagment.ATMS_WebApp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    @Autowired
    private UserSessionRepository userSessionRepo;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {
        return authService.register(request);
    }

//    @PostMapping("/login")
//    public JwtResponse login(@RequestBody LoginRequest request) {
//        String token = authService.login(request);
//        return new JwtResponse(token, null);
//    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @RequestBody LoginRequest request
    ) {
        // Check for active session
        logger.info("Check for active session");
        userSessionRepo.findByEmailAndActiveTrue(request.getUserEmail())
                .ifPresent(session -> {
                    logger.info("User already logged in");
                    throw new RuntimeException("User already logged in");
                });
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponseDTO.<LoginResponseDTO>builder()
                        .success(true)
                        .message("Login Successful")
                        .data(response)
                        .build()
        );
    }
    @PostMapping("/logout")
    public String logout(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);
        return "Logout Successfully";
    }

    @GetMapping("/get-email")
    public String getEmail(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        return authService.getEmailFromToken(token);
    }

    @GetMapping("/get-user-id")
    public Long getUserId(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        return authService.getUserIdFromToken(token);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserResponseDTO dto) {
        return authService.updateUser(id, dto);
    }
    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteSide(@PathVariable Long id) {
        return authService.deleteUser(id);
    }
    @GetMapping("/activeUsers")
    public ResponseEntity<List<UserResponseDTO>> getAllActiveUser() {
        List<UserResponseDTO> users = authService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/get-user/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        logger.info("Received request to fetch user with ID");
        UserMaster userMaster = userRepository.findById(id).orElseThrow(() -> new RuntimeException(
                messageSource.getMessage(
                        "error.user.notfound",
                        null,
                        Locale.getDefault()
                )
        ));

        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(userMaster.getUserId());
        userDto.setName(userMaster.getUserName());
        userDto.setEmail(userMaster.getEmail());
        userDto.setRole(userMaster.getRole());
        userDto.setPassword(userMaster.getPassword());

        logger.info("Fetched user details for ID");
        return userDto;
    }
}