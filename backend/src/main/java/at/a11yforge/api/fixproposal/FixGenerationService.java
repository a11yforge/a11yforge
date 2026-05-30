package at.a11yforge.api.fixproposal;

import at.a11yforge.api.scan.Scan;
import at.a11yforge.api.violation.Violation;
import at.a11yforge.api.violation.ViolationNotFoundException;
import at.a11yforge.api.violation.ViolationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FixGenerationService {

    private final FixProposalRepository fixProposalRepository;
    private final ViolationRepository violationRepository;
    private final FixGenerationAsyncRunner asyncRunner;

    public FixGenerationService(
            FixProposalRepository fixProposalRepository,
            ViolationRepository violationRepository,
            FixGenerationAsyncRunner asyncRunner
    ) {
        this.fixProposalRepository = fixProposalRepository;
        this.violationRepository = violationRepository;
        this.asyncRunner = asyncRunner;
    }

    @Transactional
    public Long requestGeneration(Long violationId) {
        Violation violation = violationRepository.findById(violationId)
                .orElseThrow(() -> new ViolationNotFoundException(violationId));

        Scan scan = violation.getPage().getScan();
        String providerName = scan.getLlmProvider().name();

        FixProposal proposal = new FixProposal(violation, providerName);
        proposal = fixProposalRepository.save(proposal);

        asyncRunner.run(proposal.getId());

        return proposal.getId();
    }
}
