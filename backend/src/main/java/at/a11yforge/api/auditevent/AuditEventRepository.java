package at.a11yforge.api.auditevent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
    java.util.List<AuditEvent> findByEntityTypeAndEntityId(String entityType, Long entityId);
}
