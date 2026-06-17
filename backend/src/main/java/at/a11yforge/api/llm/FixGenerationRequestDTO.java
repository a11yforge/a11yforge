package at.a11yforge.api.llm;

import at.a11yforge.api.violation.Impact;

public record FixGenerationRequestDTO(
    String htmlSnippet,
    String wcagRuleId,
    String targetSelector,
    String description,
    Impact impact,
    String screenshot) {}
