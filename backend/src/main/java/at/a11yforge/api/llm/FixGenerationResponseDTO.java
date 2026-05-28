package at.a11yforge.api.llm;

public record FixGenerationResponseDTO(
        String generatedHtml,
        String llmModel,
        String promptVersion,
        boolean success,
        String errorReason
) {
}
