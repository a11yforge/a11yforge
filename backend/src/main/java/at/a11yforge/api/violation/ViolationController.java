package at.a11yforge.api.violation;

import at.a11yforge.api.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/violations")
public class ViolationController {

    private final ViolationService violationService;

    public ViolationController(ViolationService violationService) {
        this.violationService = violationService;
    }

    // HTTP 200 OK
    @GetMapping
    public List<ViolationResponseDTO> getViolations(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam Long scanId) {
        return violationService.getViolationsForUser(principal.getId(), scanId);
    }

    // HTTP 200 OK
    @GetMapping("/{violationId}")
    public ViolationResponseDTO getViolation(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long violationId) {
        return violationService.getViolationByIdForUser(principal.getId(), violationId);
    }
}
