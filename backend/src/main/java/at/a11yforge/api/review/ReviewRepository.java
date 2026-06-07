package at.a11yforge.api.review;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReviewDecisionAndFixProposal_Violation_Page_Scan_Id(
            ReviewDecision decision, Long scanId);
}
