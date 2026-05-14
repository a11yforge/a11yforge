package at.a11yforge.api.violation;

public record ViolationResponseDTO(Long id, Long pageId, String ruleId, ViolationSource source, Impact impact, String htmlSnippet, String targetSelector, String description) {
}
