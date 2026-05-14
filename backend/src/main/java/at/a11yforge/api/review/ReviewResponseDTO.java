package at.a11yforge.api.review;

import at.a11yforge.api.fixproposal.FixProposal;

import java.time.Instant;

public record ReviewResponseDTO(Long id, FixProposal fixProposalId, Long userId, ReviewDecision reviewDecision, String comment, Instant decidedAt) {
}
