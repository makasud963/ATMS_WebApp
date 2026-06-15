package com.HighwayManagment.ATMS_WebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String role;

    private String password;

    private boolean active = true;
}