package at.a11yforge.api.fixproposal;

import at.a11yforge.api.llm.ChatProvider;
import at.a11yforge.api.llm.FixGenerationRequestDTO;
import at.a11yforge.api.llm.FixGenerationResponseDTO;
import at.a11yforge.api.scanner.ViolationDto;
import at.a11yforge.api.verifier.VerifierProcessRunner;
import at.a11yforge.api.verifier.VerifyRequestDTO;
import at.a11yforge.api.verifier.VerifyResultDTO;
import at.a11yforge.api.violation.Violation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FixProposalService {

  private static final Logger log = LoggerFactory.getLogger(FixProposalService.class);

  private final FixProposalRepository fixProposalRepository;
  private final VerifierProcessRunner verifierProcessRunner;

  public FixProposalService(
      FixProposalRepository fixProposalRepository, VerifierProcessRunner verifierProcessRunner) {
    this.fixProposalRepository = fixProposalRepository;
    this.verifierProcessRunner = verifierProcessRunner;
  }

  public FixProposalStatus verifyFix(
      String originalHtml,
      String newSnippet,
      List<ViolationDto> originalViolations,
      List<String> rules,
      ViolationDto targetViolation) {

    VerifyRequestDTO request =
        new VerifyRequestDTO(originalHtml, newSnippet, originalViolations, rules, targetViolation);

    VerifyResultDTO result = verifierProcessRunner.run(request);
    log.info("Verify {} -> {}", targetViolation.ruleId(), result.status());

    return result.status().equals("verified")
        ? FixProposalStatus.VERIFIED
        : FixProposalStatus.DISCARDED_VERIFICATION;
  }

  @Transactional(readOnly = true)
  public List<FixProposalResponseDTO> getFixProposalsForUser(Long userId, Long violationId) {
    return fixProposalRepository
        .findAllByViolationIdAndViolationPageScanProjectUserId(violationId, userId)
        .stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public FixProposalResponseDTO getFixProposalByIdForUser(Long userId, Long fixProposalId) {
    FixProposal fixProposal =
        fixProposalRepository
            .findByIdAndViolationPageScanProjectUserId(fixProposalId, userId)
            .orElseThrow(() -> new FixProposalNotFoundException(fixProposalId));
    return toResponse(fixProposal);
  }

  public FixProposal generateAndVerify(
      Violation violation,
      String pageHtml,
      List<ViolationDto> pageViolations,
      List<String> rules,
      ChatProvider chatProvider,
      ViolationDto targetViolation) {
    FixGenerationRequestDTO request =
        new FixGenerationRequestDTO(
            violation.getHtmlSnippet(),
            violation.getRuleId(),
            violation.getTargetSelector(),
            violation.getDescription(),
            violation.getImpact(),
            violation.getScreenshot(),
            violation.getFgColor(),
            violation.getBgColor(),
            violation.getContrastRatio(),
            violation.getExpectedContrastRatio(),
            null);

    FixGenerationResponseDTO response = chatProvider.generateFix(request);

    FixProposal fixProposal = new FixProposal(violation, chatProvider.getProviderType().name());

    if (!response.success()) {
      fixProposal.setStatus(FixProposalStatus.FAILED_PROVIDER_ERROR);
      return fixProposalRepository.save(fixProposal);
    }

    FixProposalStatus status =
        verifyFix(pageHtml, response.generatedHtml(), pageViolations, rules, targetViolation);

    fixProposal.setStatus(status);
    fixProposal.setGeneratedHtml(response.generatedHtml());
    fixProposal.setLlmModel(response.llmModel());
    fixProposal.setPromptVersion(response.promptVersion());

    return fixProposalRepository.save(fixProposal);
  }

  private FixProposalResponseDTO toResponse(FixProposal fixProposal) {
    return new FixProposalResponseDTO(
        fixProposal.getId(),
        fixProposal.getViolation().getId(),
        fixProposal.getStatus(),
        fixProposal.getGeneratedHtml(),
        fixProposal.getLlmProvider(),
        fixProposal.getLlmModel(),
        fixProposal.getPromptVersion());
  }
}
