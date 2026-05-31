package com.HighwayManagment.ATMS_WebApp.service;

import com.HighwayManagment.ATMS_WebApp.entity.AuditLog;
import com.HighwayManagment.ATMS_WebApp.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void saveAudit(String username, String action) {

        AuditLog auditLog = AuditLog.builder()
                .username(username)
                .action(action)
                .actionTime(LocalDateTime.now())
                .build();

        auditLogRepository.save(auditLog);
    }
}