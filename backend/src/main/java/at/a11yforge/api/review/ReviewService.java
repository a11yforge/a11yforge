package at.a11yforge.api.review;

import at.a11yforge.api.fixproposal.FixProposal;
import at.a11yforge.api.fixproposal.FixProposalNotFoundException;
import at.a11yforge.api.fixproposal.FixProposalRepository;
import at.a11yforge.api.user.User;
import at.a11yforge.api.user.UserNotFoundException;
import at.a11yforge.api.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final FixProposalRepository fixProposalRepository;
  private final UserRepository userRepository;

  public ReviewService(
      ReviewRepository reviewRepository,
      FixProposalRepository fixProposalRepository,
      UserRepository userRepository) {
    this.reviewRepository = reviewRepository;
    this.fixProposalRepository = fixProposalRepository;
    this.userRepository = userRepository;
  }

  public ReviewResponseDTO review(
      Long userId, Long fixProposalId, ReviewDecision decision, String comment) {

    FixProposal fixProposal =
        fixProposalRepository
            .findByIdAndViolationPageScanProjectUserId(fixProposalId, userId)
            .orElseThrow(() -> new FixProposalNotFoundException(fixProposalId));

    User user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    Review review = new Review(fixProposal, user, decision);
    review.setComment(comment);

    review = reviewRepository.save(review);

    return new ReviewResponseDTO(
        review.getId(),
        review.getFixProposal().getId(),
        review.getUser().getId(),
        review.getReviewDecision(),
        review.getComment(),
        review.getDecidedAt());
  }
}
