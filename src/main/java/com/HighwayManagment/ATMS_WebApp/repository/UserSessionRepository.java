package com.HighwayManagment.ATMS_WebApp.repository;

import com.HighwayManagment.ATMS_WebApp.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByEmailAndActiveTrue(String email);
}