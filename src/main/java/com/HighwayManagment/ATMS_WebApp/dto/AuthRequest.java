package com.HighwayManagment.ATMS_WebApp.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AuthRequest {
    private String userName;
    private String userEmail;
    private String password;
    private String role;
}