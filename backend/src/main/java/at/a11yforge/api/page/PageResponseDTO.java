package at.a11yforge.api.page;

import java.time.Instant;

public record PageResponseDTO(Long id, Long scanId, String url, Integer httpStatus, PageStatus scanStatus, Instant fetchedAt) {
}
