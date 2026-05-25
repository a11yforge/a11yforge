package at.a11yforge.api.violation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ViolationRepository extends JpaRepository<Violation, Long> {

    //Sollte noch mit findAllByPageScanIdAndPageScanProjectUserId ersetzt werden
    List<Violation> findByPage_Scan_Id(Long scanId);

    List<Violation> findAllByPageScanIdAndPageScanProjectUserId(Long scanId, Long userId);

    Optional<Violation> findByIdAndPageScanProjectUserId(Long id, Long userId);

}