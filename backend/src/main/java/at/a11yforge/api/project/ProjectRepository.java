package at.a11yforge.api.project;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByUserId(Long userId);
    Optional<Project> findByIdAndUserId(Long id, Long userId);

    List<Project> findByBaseUrl(String baseUrl);
}
