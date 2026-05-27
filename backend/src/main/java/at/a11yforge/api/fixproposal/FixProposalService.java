package at.a11yforge.api.fixproposal;

import at.a11yforge.api.scanner.ViolationDto;
import at.a11yforge.api.verifier.VerifierProcessRunner;
import at.a11yforge.api.verifier.VerifyRequestDTO;
import at.a11yforge.api.verifier.VerifyResultDTO;
import at.a11yforge.api.violation.Violation;
import org.springframework.stereotype.Service;

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
}
