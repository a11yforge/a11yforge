package at.a11yforge.api.violation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViolationRepository extends JpaRepository<Violation, Long> {

    List<Violation> findByPage_Scan_Id(Long scanId);

    List<Violation> findAllByPageScanIdAndPageScanProjectUserId(Long scanId, Long userId);

    Optional<Violation> findByIdAndPageScanProjectUserId(Long id, Long userId);

    long countByPage_Scan_Id(Long scanId);

}
