package at.a11yforge.api.probescan;

import java.util.List;

public record ProbeScanResponseDTO(List<ProbeViolationDTO> violations, int totalViolations) {}
