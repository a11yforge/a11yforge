package at.a11yforge.api.scan;

import at.a11yforge.api.violation.Impact;

public record ExportDTO(
    Long violationId,
    String ruleId,
    String targetSelector,
    String originalHtml,
    String fixedHtml,
    Impact impact,
    String description,
    boolean verified) {}
