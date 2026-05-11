package com.wtc.audit;

import com.wtc.entity.AuditLog;
import com.wtc.entity.WtcUser;
import com.wtc.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @AfterReturning(
        pointcut = "@annotation(auditable)",
        returning = "result"
    )
    public void logAudit(JoinPoint jp, Auditable auditable, Object result) {
        try {
            WtcUser user = null;
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof WtcUser u) user = u;

            AuditLog log = AuditLog.builder()
                .user(user)
                .action(auditable.action())
                .entity(auditable.entity())
                .details("Method: " + jp.getSignature().getName())
                .build();
            auditLogRepository.save(log);
        } catch (Exception e) {
            log.warn("Falha ao registrar auditoria: {}", e.getMessage());
        }
    }
}
