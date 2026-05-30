package at.a11yforge.api.fixproposal;

import at.a11yforge.api.llm.ChatProvider;
import at.a11yforge.api.llm.FixGenerationRequestDTO;
import at.a11yforge.api.llm.FixGenerationResponseDTO;
import at.a11yforge.api.scanner.ViolationDto;
import at.a11yforge.api.verifier.VerifierProcessRunner;
import at.a11yforge.api.verifier.VerifyRequestDTO;
import at.a11yforge.api.verifier.VerifyResultDTO;
import at.a11yforge.api.violation.Violation;
import at.a11yforge.api.violation.ViolationSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.List;

@Service
public class FixProposalService {

    private final FixProposalRepository fixProposalRepository;
    private final VerifierProcessRunner verifierProcessRunner;

    public FixProposalService(FixProposalRepository fixProposalRepository, VerifierProcessRunner verifierProcessRunner) {
        this.fixProposalRepository = fixProposalRepository;
        this.verifierProcessRunner = verifierProcessRunner;
    }

    public FixProposalStatus verifyFix(String originalHtml,
                                       String oldSnippet,
                                       String newSnippet,
                                       List<ViolationDto> originalViolations,
                                       List<String> rules,
                                       ViolationDto targetViolation) {

        VerifyRequestDTO request = new VerifyRequestDTO(
                originalHtml, oldSnippet, newSnippet, originalViolations, rules, targetViolation);

        VerifyResultDTO result = verifierProcessRunner.run(request);

        return result.status().equals("verified")
                ? FixProposalStatus.VERIFIED
                : FixProposalStatus.DISCARDED_VERIFICATION;

    }

    @Transactional(readOnly = true)
    public List<FixProposalResponseDTO> getFixProposalsForUser(Long userId, Long violationId) {
        return fixProposalRepository.findAllByViolationIdAndViolationPageScanProjectUserId(violationId, userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FixProposalResponseDTO getFixProposalByIdForUser(Long userId, Long fixProposalId) {
        FixProposal fixProposal = fixProposalRepository.findByIdAndViolationPageScanProjectUserId(fixProposalId, userId)
                .orElseThrow(() -> new FixProposalNotFoundException(fixProposalId));
        return toResponse(fixProposal);
    }

    private FixProposalResponseDTO toResponse(FixProposal fixProposal) {
        return new FixProposalResponseDTO(
                fixProposal.getId(),
                fixProposal.getViolation().getId(),
                fixProposal.getStatus(),
                fixProposal.getGeneratedHtml(),
                fixProposal.getLlmProvider(),
                fixProposal.getLlmModel(),
                fixProposal.getPromptVersion()
        );
    }

    public FixProposal generateAndVerify(Violation violation, String pageHtml,
                                          List<ViolationDto> pageViolations, List<String> rules,
                                          ChatProvider chatProvider, ViolationDto targetViolation) {
        FixGenerationRequestDTO request = new FixGenerationRequestDTO(violation.getHtmlSnippet(), violation.getRuleId(),
                violation.getTargetSelector(), violation.getDescription(), violation.getImpact());

        FixGenerationResponseDTO response = chatProvider.generateFix(request);

        FixProposal fixProposal = new FixProposal(violation, chatProvider.getProviderType().name());

        if(!response.success()) {
            fixProposal.setStatus(FixProposalStatus.FAILED_PROVIDER_ERROR);
            return fixProposalRepository.save(fixProposal);
        }

       FixProposalStatus status = verifyFix(pageHtml, violation.getHtmlSnippet(), response.generatedHtml(), pageViolations, rules, targetViolation);

        fixProposal.setStatus(status);
        fixProposal.setGeneratedHtml(response.generatedHtml());
        fixProposal.setLlmModel(response.llmModel());
        fixProposal.setPromptVersion(response.promptVersion());

        return fixProposalRepository.save(fixProposal);


    }

}
