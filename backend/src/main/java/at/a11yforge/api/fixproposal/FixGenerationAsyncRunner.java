package at.a11yforge.api.fixproposal;

import at.a11yforge.api.llm.ChatProvider;
import at.a11yforge.api.llm.ChatProviderFactory;
import at.a11yforge.api.llm.FixGenerationRequestDTO;
import at.a11yforge.api.llm.FixGenerationResponseDTO;
import at.a11yforge.api.llm.ProviderType;
import at.a11yforge.api.scan.Scan;
import at.a11yforge.api.violation.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FixGenerationAsyncRunner {

    private static final Logger log = LoggerFactory.getLogger(FixGenerationAsyncRunner.class);

    private final FixProposalRepository fixProposalRepository;
    private final ChatProviderFactory chatProviderFactory;

    public FixGenerationAsyncRunner(
            FixProposalRepository fixProposalRepository,
            ChatProviderFactory chatProviderFactory
    ) {
        this.fixProposalRepository = fixProposalRepository;
        this.chatProviderFactory = chatProviderFactory;
    }

    @Async
    @Transactional
    public void run(Long fixProposalId) {
        FixProposal proposal = fixProposalRepository.findById(fixProposalId)
                .orElseThrow(() -> new IllegalStateException(
                        "FixProposal not found: " + fixProposalId));

        Violation violation = proposal.getViolation();
        Scan scan = violation.getPage().getScan();
        ProviderType providerType = scan.getLlmProvider();

        ChatProvider provider = chatProviderFactory.getProvider(providerType);

        FixGenerationRequestDTO request = new FixGenerationRequestDTO(
                violation.getHtmlSnippet(),
                violation.getRuleId(),
                violation.getTargetSelector(),
                violation.getDescription(),
                violation.getImpact()
        );

        FixGenerationResponseDTO response = provider.generateFix(request);

        applyResponse(proposal, response, providerType);
        fixProposalRepository.save(proposal);

        log.info("FixProposal {} finished with status {}", proposal.getId(), proposal.getStatus());
    }

    private void applyResponse(FixProposal proposal, FixGenerationResponseDTO response,
                               ProviderType providerType) {
        if (providerType == ProviderType.NONE) {
            proposal.setStatus(FixProposalStatus.SKIPPED_NO_PROVIDER);
            return;
        }

        if (!response.success()) {
            proposal.setStatus(FixProposalStatus.FAILED_PROVIDER_ERROR);
            log.warn("FixProposal {} failed: {}", proposal.getId(), response.errorReason());
            return;
        }

        proposal.setGeneratedHtml(response.generatedHtml());
        proposal.setLlmModel(response.llmModel());
        proposal.setPromptVersion(response.promptVersion());
        proposal.setStatus(FixProposalStatus.VERIFIED);
    }
}
