package at.a11yforge.api.probescan;

import at.a11yforge.api.scanner.PageScanResultDto;
import at.a11yforge.api.scanner.ScannerProcessRunner;
import at.a11yforge.api.violation.Impact;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProbeScanService {

  private final ScannerProcessRunner scannerProcessRunner;

  public ProbeScanService(ScannerProcessRunner scannerProcessRunner) {
    this.scannerProcessRunner = scannerProcessRunner;
  }

  public ProbeScanResponseDTO probeScan(String url) {
    List<String> rules =
        List.of("image-alt", "color-contrast", "label", "html-has-lang", "heading-order");
    List<PageScanResultDto> results = scannerProcessRunner.run(url, rules, 1);

    List<ProbeViolationDTO> all =
        results.stream()
            .flatMap(r -> r.violations().stream())
            .map(
                v ->
                    new ProbeViolationDTO(
                        v.ruleId(),
                        Impact.valueOf(v.impact().toUpperCase()),
                        v.description(),
                        v.target().isEmpty() ? null : v.target().get(0)))
            .toList();

    return new ProbeScanResponseDTO(all.stream().limit(5).toList(), all.size());
  }
}
