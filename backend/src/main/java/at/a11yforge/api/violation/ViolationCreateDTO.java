package at.a11yforge.api.violation;

public record ViolationCreateDTO(
              Long pageId,
              String ruleId,
              ViolationSource source,
              Impact impact,
              String htmlSnippet,
              String targetSelector,
              String description) {
}
