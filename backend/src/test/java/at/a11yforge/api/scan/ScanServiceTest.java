package at.a11yforge.api.scan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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

  @Autowired ScanService scanService;
  @Autowired UserRepository userRepository;
  @Autowired ProjectRepository projectRepository;

  @Test
  void scanPersistsViolations() {
    String unique = "u" + System.nanoTime();
    User user = userRepository.save(new User(unique + "@test.at", "x", unique));
    Project project =
        projectRepository.save(
            new Project(
                user,
                "Test",
                "file:///Users/maxmayer/dev/a11yforge/scanner/src/tests/missing-alt.html"));

    ScanResponseDTO result =
        scanService.createAndRunScan(user.getId(), project.getId());
    Long scanId = result.id();
    ScanDetailDTO detail = scanService.getScan(scanId, user.getId());

    assertThat(result.status()).isEqualTo("COMPLETED");
    assertThat(detail.violations()).hasSize(3);

    assertThat(detail.violations().get(0).ruleId()).isEqualTo("image-alt");
  }

  @Test
  void foreignUserCannotAccessScan() {
    String a = "a" + System.nanoTime();
    String b = "b" + System.nanoTime();
    User userA = userRepository.save(new User(a + "@test.at", "x", a));
    User userB = userRepository.save(new User(b + "@test.at", "x", b));

    Project project =
        projectRepository.save(
            new Project(
                userA,
                "Test",
                "file:///Users/maxmayer/dev/a11yforge/scanner/src/tests/missing-alt.html"));

    ScanResponseDTO result =
        scanService.createAndRunScan(userA.getId(), project.getId());
    Long scanId = result.id();

    assertThatThrownBy(() -> scanService.getScan(scanId, userB.getId()))
        .isInstanceOf(ScanNotFoundException.class);
  }
}
