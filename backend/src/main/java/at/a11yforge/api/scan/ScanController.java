package at.a11yforge.api.scan;

import at.a11yforge.api.security.CustomUserDetails;
import java.util.List;
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
    return scanService.startScan(principal.getId(), dto.projectId());
  }

  @GetMapping("/{id}")
  public ScanDetailDTO getScan(
      @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
    return scanService.getScan(principal.getId(), id);
  }

  @GetMapping
  public List<ScanResponseDTO> getScans(
      @AuthenticationPrincipal CustomUserDetails principal, @RequestParam Long projectId) {
    return scanService.getScansForProject(principal.getId(), projectId);
  }

  @GetMapping("/{id}/export")
  public List<ExportDTO> exportFixes(
      @AuthenticationPrincipal CustomUserDetails principal, @PathVariable Long id) {
    return scanService.exportFixes(principal.getId(), id);
  }
}
