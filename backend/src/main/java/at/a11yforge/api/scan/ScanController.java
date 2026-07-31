package at.a11yforge.api.scan;

import at.a11yforge.api.ratelimit.RateLimiterService;
import at.a11yforge.api.security.CustomUserDetails;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scan")
public class ScanController {

  private final ScanService scanService;
  private final RateLimiterService rateLimiter;
  private final int userPerHour;

  public ScanController(ScanService scanService, RateLimiterService rateLimiter, @Value("${a11yforge.ratelimit.user-per-hour}") int userPerHour) {
    this.scanService = scanService;
    this.rateLimiter = rateLimiter;
    this.userPerHour = userPerHour;
  }

  @PostMapping
  public ScanResponseDTO startScan(
      @AuthenticationPrincipal CustomUserDetails principal, @RequestBody ScanRequestDTO dto) {
    rateLimiter.check("user:" + principal.getId(), userPerHour);
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
    return scanService.exportReviewedFixes(principal.getId(), id);
  }
}
