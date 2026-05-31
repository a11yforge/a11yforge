package at.a11yforge.api.scan;

import static org.assertj.core.api.Assertions.assertThat;

import at.a11yforge.api.llm.ProviderType;
import at.a11yforge.api.project.Project;
import at.a11yforge.api.project.ProjectRepository;
import at.a11yforge.api.user.User;
import at.a11yforge.api.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
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

        ScanResponseDTO result = scanService.createAndRunScan(project.getId(), ProviderType.NONE);
        Long scanId = result.id();
        ScanDetailDTO detail = scanService.getScan(scanId);




        assertThat(result.status()).isEqualTo("COMPLETED");
        assertThat(detail.violations()).hasSize(3);

        assertThat(detail.violations().get(0).ruleId()).isEqualTo("image-alt");
    }
}
