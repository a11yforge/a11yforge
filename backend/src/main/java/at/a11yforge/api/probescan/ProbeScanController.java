package at.a11yforge.api.probescan;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/probescan")
public class ProbeScanController {

  private final ProbeScanService probeScanService;

  public ProbeScanController(ProbeScanService probeScanService) {
    this.probeScanService = probeScanService;
  }

  @PostMapping
  public ProbeScanResponseDTO startScan(@RequestBody ProbeScanRequestDTO dto) {
    return probeScanService.probeScan(dto.url());
  }
}
