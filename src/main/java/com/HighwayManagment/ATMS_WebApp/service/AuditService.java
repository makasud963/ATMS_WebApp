package com.HighwayManagment.ATMS_WebApp.service;

import com.HighwayManagment.ATMS_WebApp.entity.AuditLog;
import com.HighwayManagment.ATMS_WebApp.entity.UserContext;
import com.HighwayManagment.ATMS_WebApp.repository.AuditLogRepository;
import com.HighwayManagment.ATMS_WebApp.security.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void saveAudit(
            String tableName,
            String recordId,
            String actionType
    ) {

        UserContext context =
                UserContextHolder.get();

        AuditLog auditLog =
                AuditLog.builder()
                        .tableName(tableName)
                        .recordId(recordId)
                        .actionType(actionType)
                        .sessionId(
                                context != null
                                        ? context.getSessionId()
                                        : null
                        )
                        .actionTime(LocalDateTime.now())
                        .build();
        System.out.println(
                "AUDIT -> "
                        + tableName
                        + " | "
                        + recordId
                        + " | "
                        + actionType
        );
        auditLogRepository.save(auditLog);
    }
}