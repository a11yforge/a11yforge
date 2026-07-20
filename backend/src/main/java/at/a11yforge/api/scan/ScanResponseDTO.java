package at.a11yforge.api.scan;

import java.time.Instant;

public record ScanResponseDTO(
    Long id,
    Long projectId,
    String status,
    Instant startedAt,
    Instant completedAt,
    long violationCount,
    long projectScanNumber) {}
