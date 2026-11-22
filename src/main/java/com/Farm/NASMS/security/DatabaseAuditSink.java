package com.Farm.NASMS.security;

import com.Farm.NASMS.Repository.AuditLogRepository;
import com.Farm.NASMS.model.AuditLog;
import com.walter.toolkit.security_starter.audit.AuditSink;
import com.walter.toolkit.security_starter.dto.AuditEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Component
public class DatabaseAuditSink implements AuditSink {
    private final AuditLogRepository auditLogRepository;

    public DatabaseAuditSink(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(AuditEvent event) {
        AuditLog log = AuditLog.builder()
                .actor(event.actor())
                .action(event.action())
                .methodName(event.methodName())
                .status(event.status())
                .details(event.details())
                .timestamp(event.timeStamp())
                .build();

        auditLogRepository.save(log);
    }
}
