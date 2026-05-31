package com.HighwayManagment.ATMS_WebApp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String userName;
    @Column(name = "user_email_id", unique = true, nullable = false)
    private String email;
    private String password;
    private String role;
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}