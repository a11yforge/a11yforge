package at.a11yforge.api.review;

public record ReviewCreateDTO(Long fixProposalId,
                              ReviewDecision reviewDecision,
                              String comment) {
}
