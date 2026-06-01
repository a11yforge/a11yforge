package at.a11yforge.api.review;

public record ReviewRequestDTO(Long fixProposalId, ReviewDecision decision, String comment) {}
