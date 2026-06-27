package at.a11yforge.api.scanner;

import java.util.List;

// vertrag mit types.ts
public record ViolationDto(
    String violationId,
    String source,
    String ruleId,
    String impact,
    List<String> wcagTags,
    String helpUrl,
    String description,
    String failureSummary,
    List<String> target,
    String htmlSnippet,
    String domPath,
    String screenshot,
    String detectedLang) {}
