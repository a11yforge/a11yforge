package at.a11yforge.api.auditevent;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuditEventService {

    private final AuditEventRepository auditEventRepository;

    public AuditEventService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    @Transactional
    public void recordEvent(Long userId, String entityType, Long entityId,
                            AuditEventType eventType, String oldValue, String newValue) {
        AuditEvent event = new AuditEvent();
        event.setUserId(userId);
        event.setEntityType(entityType);
        event.setEntityId(entityId);
        event.setEventType(eventType);
        event.setOldValue(oldValue);
        event.setNewValue(newValue);
        event.setTimestamp(Instant.now());
        auditEventRepository.save(event);
    }
}
