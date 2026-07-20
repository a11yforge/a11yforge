package at.a11yforge.api.scan;

import at.a11yforge.api.violation.ViolationResponseDTO;
import java.time.Instant;
import java.util.List;

public record ScanDetailDTO(Long id,
                            Long projectId,
                            String status,
                            Instant startedAt,
                            Instant completedAt,
                            List<ViolationResponseDTO> violations,
                            List<ViolationFixDTO> fixes,
                            Long projectScanNumber) {}
