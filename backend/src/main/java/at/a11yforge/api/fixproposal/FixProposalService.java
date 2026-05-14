package at.a11yforge.api.fixproposal;

import org.springframework.stereotype.Service;

@Service
public class FixProposalService {

    private final FixProposalRepository fixProposalRepository;

    public FixProposalService(FixProposalRepository fixProposalRepository) {
        this.fixProposalRepository = fixProposalRepository;
    }
}
