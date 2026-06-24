package at.a11yforge.api.probescan;

import at.a11yforge.api.violation.Impact;

public record ProbeViolationDTO(
    String ruleId, Impact impact, String description, String targetSelector) {}
