package at.a11yforge.api.scan;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepository extends JpaRepository<Scan, Long> {

  Optional<Scan> findByIdAndProjectUserId(Long id, Long userId);

  List<Scan> findAllByProjectIdAndProjectUserId(Long projectId, Long userId);
}
