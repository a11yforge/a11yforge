package at.a11yforge.api.fixproposal;

public record FixProposalResponseDTO(Long id,
                                     Long violationId,
                                     FixProposalStatus status,
                                     String generatedHtml,
                                     String llmProvider,
                                     String llmModel,
                                     String promptVersion) {
}
