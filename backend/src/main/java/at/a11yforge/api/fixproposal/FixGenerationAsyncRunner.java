package at.a11yforge.api.fixproposal;

import at.a11yforge.api.auditevent.AuditEventService;
import at.a11yforge.api.auditevent.AuditEventType;
import at.a11yforge.api.fixcache.FixCacheService;
import at.a11yforge.api.llm.ChatProvider;
import at.a11yforge.api.llm.ChatProviderFactory;
import at.a11yforge.api.llm.FixGenerationRequestDTO;
import at.a11yforge.api.llm.FixGenerationResponseDTO;
import at.a11yforge.api.llm.ProviderType;
import at.a11yforge.api.scan.Scan;
import at.a11yforge.api.scanner.ViolationDto;
import at.a11yforge.api.violation.Violation;
import at.a11yforge.api.violation.ViolationRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FixGenerationAsyncRunner {

  private static final Logger log = LoggerFactory.getLogger(FixGenerationAsyncRunner.class);

  private final FixProposalRepository fixProposalRepository;
  private final ChatProviderFactory chatProviderFactory;
  private final FixProposalService fixProposalService;
  private final ViolationRepository violationRepository;
  private final FixCacheService fixCacheService;
  private final AuditEventService auditEventService;

  public FixGenerationAsyncRunner(
      FixProposalRepository fixProposalRepository,
      ChatProviderFactory chatProviderFactory,
      FixProposalService fixProposalService,
      ViolationRepository violationRepository,
      FixCacheService fixCacheService,
      AuditEventService auditEventService) {
    this.fixProposalRepository = fixProposalRepository;
    this.chatProviderFactory = chatProviderFactory;
    this.fixProposalService = fixProposalService;
    this.violationRepository = violationRepository;
    this.fixCacheService = fixCacheService;
    this.auditEventService = auditEventService;
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("fixGenerationExecutor")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handle(FixGenerationRequestedEvent event) {
    Long fixProposalId = event.fixProposalId();
    FixProposal proposal =
        fixProposalRepository
            .findById(fixProposalId)
            .orElseThrow(
                () -> new IllegalStateException("FixProposal not found: " + fixProposalId));

    String oldStatus = proposal.getStatus().name();

    Violation violation = proposal.getViolation();
    Scan scan = violation.getPage().getScan();
    ProviderType providerType = scan.getLlmProvider();

    if (providerType == ProviderType.NONE) {
      proposal.setStatus(FixProposalStatus.SKIPPED_NO_PROVIDER);
      fixProposalRepository.save(proposal);
      return;
    }

    ChatProvider provider = chatProviderFactory.getProvider(providerType);

    FixGenerationRequestDTO request =
        new FixGenerationRequestDTO(
            violation.getHtmlSnippet(),
            violation.getRuleId(),
            violation.getTargetSelector(),
            violation.getDescription(),
            violation.getImpact(),
            violation.getScreenshot());

    Optional<String> cached =
        fixCacheService.findCachedFix(violation.getRuleId(), violation.getHtmlSnippet());

    String generatedHtml;
    String llmModel;
    String promptVersion;
    boolean fromCache = cached.isPresent();

    if (violation.getRuleId().equals("html-has-lang")) {
      generatedHtml = buildLangFix(violation.getHtmlSnippet(), violation.getDetectedLang());
      llmModel = "deterministic";
      promptVersion = "deterministic";
    } else if (fromCache) {
      generatedHtml = cached.get();
      llmModel = "cache";
      promptVersion = "cache";
      log.info("FixProposal {} cache hit", proposal.getId());
    } else {
      FixGenerationResponseDTO response = provider.generateFix(request);
      if (!response.success()) {
        proposal.setStatus(FixProposalStatus.FAILED_PROVIDER_ERROR);
        log.warn("FixProposal {} failed: {}", proposal.getId(), response.errorReason());
        fixProposalRepository.save(proposal);
        return;
      }
      generatedHtml = response.generatedHtml();
      llmModel = response.llmModel();
      promptVersion = response.promptVersion();
    }

    String pageHtml = violation.getPage().getRenderedHtml();
    List<Violation> pageEntities = violationRepository.findByPage_Scan_Id(scan.getId());
    List<ViolationDto> pageViolations = pageEntities.stream().map(this::toDto).toList();
    List<String> rules = pageEntities.stream().map(Violation::getRuleId).distinct().toList();
    ViolationDto targetViolation = toDto(violation);

    FixProposalStatus status =
        fixProposalService.verifyFix(
            pageHtml,
            violation.getHtmlSnippet(),
            generatedHtml,
            pageViolations,
            rules,
            targetViolation);

    proposal.setGeneratedHtml(generatedHtml);
    proposal.setLlmModel(llmModel);
    proposal.setPromptVersion(promptVersion);
    proposal.setStatus(status);
    fixProposalRepository.save(proposal);

    if (!fromCache && status == FixProposalStatus.VERIFIED) {
      fixCacheService.cacheFix(violation.getRuleId(), violation.getHtmlSnippet(), generatedHtml);
    }

    log.info("FixProposal {} finished with status {}", proposal.getId(), proposal.getStatus());

    Long ownerId = scan.getProject().getUser().getId();
    auditEventService.recordEvent(
        ownerId,
        "FixProposal",
        proposal.getId(),
        AuditEventType.STATUS_CHANGED,
        oldStatus,
        status.name());
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
          v.getScreenshot(),
          null,
          v.getDetectedLang(),
          v.getFgColor(),
          v.getBgColor(),
          v.getContrastRatio(),
          v.getExpectedContrastRatio());
    }

  private String buildLangFix(String htmlSnippet, String detectedLang) {
    if (detectedLang == null || detectedLang.isBlank()) {
      return htmlSnippet;
    }
    return htmlSnippet.replaceFirst("<html", "<html lang=\"" + detectedLang + "\"");
  }
}
