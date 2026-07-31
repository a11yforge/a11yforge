package at.a11yforge.api.probescan;

import at.a11yforge.api.ratelimit.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/probescan")
public class ProbeScanController {

  private final ProbeScanService probeScanService;
  private final RateLimiterService rateLimiter;
  private final int probePerHour;

  public ProbeScanController(
      ProbeScanService probeScanService,
      RateLimiterService rateLimiter,
      @Value("${a11yforge.ratelimit.probe-per-hour}") int probePerHour) {
    this.probeScanService = probeScanService;
    this.rateLimiter = rateLimiter;
    this.probePerHour = probePerHour;
  }

  @PostMapping
  public ProbeScanResponseDTO startScan(
      @RequestBody ProbeScanRequestDTO dto, HttpServletRequest request) {
    rateLimiter.check("ip:" + request.getRemoteAddr(), probePerHour);
    return probeScanService.probeScan(dto.url());
  }
}
