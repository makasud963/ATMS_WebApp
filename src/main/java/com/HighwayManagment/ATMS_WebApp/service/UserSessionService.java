package com.HighwayManagment.ATMS_WebApp.service;

import com.HighwayManagment.ATMS_WebApp.entity.UserSession;
import com.HighwayManagment.ATMS_WebApp.repository.UserSessionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public UserSession createSession(String email, String machineId) {

        UserSession session = UserSession.builder()
                .email(email)
                .machineId(machineId)
                .active(true)
                .loginTime(LocalDateTime.now())
                .build();

        return userSessionRepository.save(session);
    }

    public void logoutSession(Long sessionId) {

        UserSession session = userSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session Not Found"));

        session.setActive(false);
        session.setLogoutTime(LocalDateTime.now());

        userSessionRepository.save(session);
    }
}
