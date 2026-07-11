package at.a11yforge.api.review;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  List<Review> findByFixProposal_Violation_Page_Scan_Id(Long scanId);

  @Query(
      """
        select count(r) > 0 from Review r
        where r.reviewDecision = at.a11yforge.api.review.ReviewDecision.REJECTED
          and r.fixProposal.violation.page.scan.project.id = :projectId
          and r.fixProposal.violation.ruleId = :ruleId
          and r.fixProposal.violation.htmlSnippet = :htmlSnippet
        """)
  boolean existsRejectedForProjectAndContent(
      @Param("projectId") Long projectId,
      @Param("ruleId") String ruleId,
      @Param("htmlSnippet") String htmlSnippet);
}
