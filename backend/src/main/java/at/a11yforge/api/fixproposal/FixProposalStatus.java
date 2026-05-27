package at.a11yforge.api.fixproposal;

public enum FixProposalStatus {
    PENDING,
    VERIFIED,
    DISCARDED_HALLUCINATION,
    DISCARDED_VERIFICATION,
    SKIPPED_NO_PROVIDER,
    FAILED_PROVIDER_ERROR
}
