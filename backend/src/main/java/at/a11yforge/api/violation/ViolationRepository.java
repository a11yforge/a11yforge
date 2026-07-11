package at.a11yforge.api.violation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViolationRepository extends JpaRepository<Violation, Long> {

    List<Violation> findByPage_Scan_Id(Long scanId);

    List<Violation> findAllByPageScanIdAndPageScanProjectUserId(Long scanId, Long userId);

    // axe "incomplete" (cantTell) sind keine Verstöße, sondern Prüfhinweise –
    // aus Liste und Zählung ausschließen, aber in der DB behalten.
    List<Violation> findAllByPageScanIdAndPageScanProjectUserIdAndSourceNot(
            Long scanId, Long userId, ViolationSource source);

    Optional<Violation> findByIdAndPageScanProjectUserId(Long id, Long userId);

    long countByPage_Scan_Id(Long scanId);

    long countByPage_Scan_IdAndSourceNot(Long scanId, ViolationSource source);

}
