package at.a11yforge.api.page;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {

    List<Page> findAllByScanIdAndScanProjectUserId(Long scanId, Long userId);

    Optional<Page> findByIdAndScanProjectUserId(Long id, Long userId);
}