package at.a11yforge.api.fixcache;

import java.time.Instant;

public record FixCacheResponseDTO(Long id, String violationHash, String ruleId, String cachedFixHtml, Integer hitCount, Instant createdAt) {
}
