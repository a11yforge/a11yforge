package at.a11yforge.api.auditevent;

import java.time.Instant;

public record AuditEventResponseDTO(Long id, Long userId, String entityType, Long entityId, String eventType, String oldValue, String newValue, Instant timestamp) {
}
