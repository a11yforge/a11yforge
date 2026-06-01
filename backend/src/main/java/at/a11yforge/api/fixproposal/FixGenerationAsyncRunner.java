package at.a11yforge.api.fixproposal;

import at.a11yforge.api.llm.ChatProvider;
import at.a11yforge.api.llm.ChatProviderFactory;
import at.a11yforge.api.llm.FixGenerationRequestDTO;
import at.a11yforge.api.llm.FixGenerationResponseDTO;
import at.a11yforge.api.llm.ProviderType;
import at.a11yforge.api.scan.Scan;
import at.a11yforge.api.scanner.ViolationDto;
import at.a11yforge.api.violation.Violation;
import at.a11yforge.api.violation.ViolationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
public class FixGenerationAsyncRunner {

    private static final Logger log = LoggerFactory.getLogger(FixGenerationAsyncRunner.class);

    private final FixProposalRepository fixProposalRepository;
    private final ChatProviderFactory chatProviderFactory;
    private final FixProposalService fixProposalService;
    private final ViolationRepository violationRepository;

    public FixGenerationAsyncRunner(
            FixProposalRepository fixProposalRepository,
            ChatProviderFactory chatProviderFactory,
            FixProposalService fixProposalService,
            ViolationRepository violationRepository
    ) {
        this.fixProposalRepository = fixProposalRepository;
        this.chatProviderFactory = chatProviderFactory;
        this.fixProposalService = fixProposalService;
        this.violationRepository = violationRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("fixGenerationExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(FixGenerationRequestedEvent event) {
        Long fixProposalId = event.fixProposalId();
        FixProposal proposal = fixProposalRepository.findById(fixProposalId)
                .orElseThrow(() -> new IllegalStateException(
                        "FixProposal not found: " + fixProposalId));

        Violation violation = proposal.getViolation();
        Scan scan = violation.getPage().getScan();
        ProviderType providerType = scan.getLlmProvider();

        if (providerType == ProviderType.NONE) {
            proposal.setStatus(FixProposalStatus.SKIPPED_NO_PROVIDER);
            fixProposalRepository.save(proposal);
            return;
        }

        ChatProvider provider = chatProviderFactory.getProvider(providerType);

        FixGenerationRequestDTO request = new FixGenerationRequestDTO(
                violation.getHtmlSnippet(),
                violation.getRuleId(),
                violation.getTargetSelector(),
                violation.getDescription(),
                violation.getImpact()
        );

        FixGenerationResponseDTO response = provider.generateFix(request);

        if (!response.success()) {
            proposal.setStatus(FixProposalStatus.FAILED_PROVIDER_ERROR);
            log.warn("FixProposal {} failed: {}", proposal.getId(), response.errorReason());
            fixProposalRepository.save(proposal);
            return;
        }

        // Verify-Input bauen
        String pageHtml = violation.getPage().getRenderedHtml();
        List<Violation> pageEntities = violationRepository.findByPage_Scan_Id(scan.getId());
        List<ViolationDto> pageViolations = pageEntities.stream().map(this::toDto).toList();
        List<String> rules = pageEntities.stream().map(Violation::getRuleId).distinct().toList();
        ViolationDto targetViolation = toDto(violation);

        FixProposalStatus status = fixProposalService.verifyFix(
                pageHtml,
                violation.getHtmlSnippet(),
                response.generatedHtml(),
                pageViolations,
                rules,
                targetViolation);

        proposal.setGeneratedHtml(response.generatedHtml());
        proposal.setLlmModel(response.llmModel());
        proposal.setPromptVersion(response.promptVersion());
        proposal.setStatus(status);
        fixProposalRepository.save(proposal);

        log.info("FixProposal {} finished with status {}", proposal.getId(), proposal.getStatus());
    }

    private ViolationDto toDto(Violation v) {
        return new ViolationDto(
                String.valueOf(v.getId()),
                v.getSource().name(),
                v.getRuleId(),
                v.getImpact().name(),
                List.of(),
                "",
                v.getDescription(),
                "",
                v.getTargetSelector() == null ? List.of() : List.of(v.getTargetSelector()),
                v.getHtmlSnippet(),
                null);
    }
}