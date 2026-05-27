package at.a11yforge.api.fixproposal;

import at.a11yforge.api.verifier.VerifierProcessRunner;
import org.springframework.stereotype.Service;

@Service
public class FixProposalService {

    private final FixProposalRepository fixProposalRepository;
    private final VerifierProcessRunner verifierProcessRunner;

    public FixProposalService(FixProposalRepository fixProposalRepository, VerifierProcessRunner verifierProcessRunner) {
        this.fixProposalRepository = fixProposalRepository;
        this.verifierProcessRunner = verifierProcessRunner;
    }
}
