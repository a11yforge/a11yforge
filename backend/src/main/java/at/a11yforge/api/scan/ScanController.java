package at.a11yforge.api.scan;

import at.a11yforge.api.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scan")
public class ScanController {

  private final ScanService scanService;

  public ScanController(ScanService scanService) {
    this.scanService = scanService;
  }

  @PostMapping
  public ScanResponseDTO startScan(
      @AuthenticationPrincipal CustomUserDetails principal, @RequestBody ScanRequestDTO dto) {
    return scanService.createAndRunScan(principal.getId(), dto.projectId(), dto.llmProvider());
  }

  @GetMapping("/{id}")
  public ScanDetailDTO getScan(
      @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
    return scanService.getScan(id, principal.getId());
  }
}
