package at.a11yforge.api.review;

import at.a11yforge.api.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping
  public ReviewResponseDTO createReview(
      @AuthenticationPrincipal CustomUserDetails principal, @RequestBody ReviewRequestDTO request) {
    return reviewService.review(
        principal.getId(), request.fixProposalId(), request.decision(), request.comment());
  }
}
