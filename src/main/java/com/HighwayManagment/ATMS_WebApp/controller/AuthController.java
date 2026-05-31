package com.HighwayManagment.ATMS_WebApp.controller;

import com.HighwayManagment.ATMS_WebApp.dto.*;
import com.HighwayManagment.ATMS_WebApp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
}