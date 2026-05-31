package com.HighwayManagment.ATMS_WebApp.dto;
import lombok.Data;

@Data
public class LoginRequest {

    private String userEmail;
    private String password;
    private String machineId;
}
