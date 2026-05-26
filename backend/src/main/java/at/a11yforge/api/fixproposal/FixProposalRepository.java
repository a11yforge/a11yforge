package at.a11yforge.api.fixproposal;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FixProposalRepository extends JpaRepository<FixProposal, Long> {

    List<FixProposal> findAllByViolationIdAndViolationPageScanProjectUserId(Long violationId, Long userId);

    Optional<FixProposal> findByIdAndViolationPageScanProjectUserId(Long id, Long userId);
}