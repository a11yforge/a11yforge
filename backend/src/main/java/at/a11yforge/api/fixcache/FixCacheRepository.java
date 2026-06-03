package at.a11yforge.api.fixcache;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixCacheRepository extends JpaRepository<FixCache, Long> {

  Optional<FixCache> findByViolationHash(String violationHash);
}
