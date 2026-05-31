package com.HighwayManagment.ATMS_WebApp.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

    private String accessToken;

    private String refreshToken;

    private UserResponseDTO user;
}

