package at.a11yforge.api.scan;

public record ExportDTO(
    Long violationId,
    String ruleId,
    String targetSelector,
    String originalHtml,
    String fixedHtml) {}
