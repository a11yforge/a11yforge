package at.a11yforge.api.project;

import java.time.Instant;

public record ProjectResponseDTO(
    Long id,
    String name,
    String baseUrl,
    Integer crawlMaxPages,
    Instant createdAt,
    Instant updatedAt,
    Instant lastScanAt,
    Long findings) {}
