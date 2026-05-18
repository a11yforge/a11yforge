package at.a11yforge.api.scan;

import at.a11yforge.api.project.Project;
import at.a11yforge.api.project.ProjectRepository;
import at.a11yforge.api.user.User;
import at.a11yforge.api.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class ScanServiceTest {

    @Autowired
    ScanService scanService;
    @Autowired UserRepository userRepository;
    @Autowired ProjectRepository projectRepository;


    @Test
    void scanPersistsViolations() {
        User user = userRepository.save(new User("test@test.at", "x", "tester"));
        Project project = projectRepository.save(new Project(
                user, "Test", "file:///Users/maxmayer/dev/a11yforge/scanner/src/tests/missing-alt.html"));

        ScanResponseDTO result = scanService.createAndRunScan(project.getId());

        assertThat(result.status()).isEqualTo("COMPLETED");
    }
}
