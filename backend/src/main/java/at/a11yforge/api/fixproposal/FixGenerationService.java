package at.a11yforge.api.fixproposal;

import at.a11yforge.api.scan.Scan;
import at.a11yforge.api.violation.Violation;
import at.a11yforge.api.violation.ViolationNotFoundException;
import at.a11yforge.api.violation.ViolationRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FixGenerationService {

    private final FixProposalRepository fixProposalRepository;
    private final ViolationRepository violationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public FixGenerationService(
            FixProposalRepository fixProposalRepository,
            ViolationRepository violationRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.fixProposalRepository = fixProposalRepository;
        this.violationRepository = violationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Long requestGeneration(Long userId, Long violationId) {
        Violation violation = violationRepository.findById(violationId)
                .orElseThrow(() -> new ViolationNotFoundException(violationId));

        Scan scan = violation.getPage().getScan();
        Long ownerId = scan.getProject().getUser().getId();
        if (!ownerId.equals(userId)) {
            throw new ViolationNotFoundException(violationId);
        }

        String providerName = scan.getLlmProvider().name();

        FixProposal proposal = new FixProposal(violation, providerName);
        proposal = fixProposalRepository.save(proposal);

        eventPublisher.publishEvent(new FixGenerationRequestedEvent(proposal.getId()));

        return proposal.getId();
    }
}
