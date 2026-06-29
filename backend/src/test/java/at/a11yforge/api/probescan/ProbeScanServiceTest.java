package at.a11yforge.api.probescan;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import at.a11yforge.api.scanner.PageScanResultDto;
import at.a11yforge.api.scanner.ScannerProcessRunner;
import at.a11yforge.api.scanner.ViolationDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProbeScanServiceTest {

  @Mock ScannerProcessRunner scannerProcessRunner;
  @InjectMocks private ProbeScanService probeScanService;

  private ViolationDto fakeViolation() {
    return new ViolationDto(
        "v1",
        "axe_violation",
        "image-alt",
        "critical",
        List.of(),
        "",
        "fehlt alt",
        "",
        List.of("img"),
        "<img>",
        "",
        null,
      null,
      null,
      null,
      null,
        null,
        null);
  }

  @Test
  public void teaserCutsToFiveButKeepsTotalCount() {

    List<ViolationDto> violations = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      violations.add(fakeViolation());
    }

    PageScanResultDto page =
        new PageScanResultDto(
            "1.0",
            "https://x",
            "https://x",
            "now",
            0L,
            "OK",
            null,
            200,
            "Titel",
            List.of(),
            "<html></html>",
            violations,
            List.of());

    when(scannerProcessRunner.run(any(), any(), anyInt())).thenReturn(List.of(page));

    ProbeScanResponseDTO result = probeScanService.probeScan("https://test");

    assertThat(result.violations()).hasSize(5);
    assertThat(result.totalViolations()).isEqualTo(8);
  }
}
