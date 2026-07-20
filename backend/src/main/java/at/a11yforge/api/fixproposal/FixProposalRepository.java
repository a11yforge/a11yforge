package at.a11yforge.api.fixproposal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixProposalRepository extends JpaRepository<FixProposal, Long> {

  List<FixProposal> findAllByViolationIdAndViolationPageScanProjectUserId(
      Long violationId, Long userId);

  Optional<FixProposal> findByIdAndViolationPageScanProjectUserId(Long id, Long userId);

  List<FixProposal> findAllByViolationPageScanId(Long scanId);
}
