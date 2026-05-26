package at.a11yforge.api.fixproposal;

public class FixProposalNotFoundException extends RuntimeException {

    public FixProposalNotFoundException(Long fixProposalId) {
        super("FixProposal with id " + fixProposalId + " not found");
    }
}
