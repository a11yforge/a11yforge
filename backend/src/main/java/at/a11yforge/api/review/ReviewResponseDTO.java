package at.a11yforge.api.review;


import java.time.Instant;

public record ReviewResponseDTO(
    Long id,
    Long fixProposalId,
    Long userId,
    ReviewDecision reviewDecision,
    String comment,
    Instant decidedAt) {}
