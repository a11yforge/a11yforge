package at.a11yforge.api.fixproposal;

import at.a11yforge.api.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fix-proposals")
public class FixProposalController {

    private final FixProposalService fixProposalService;

    public FixProposalController(FixProposalService fixProposalService) {
        this.fixProposalService = fixProposalService;
    }

    // HTTP 200 OK
    @GetMapping
    public List<FixProposalResponseDTO> getFixProposals(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam Long violationId) {
        return fixProposalService.getFixProposalsForUser(principal.getId(), violationId);
    }

    // HTTP 200 OK
    @GetMapping("/{fixProposalId}")
    public FixProposalResponseDTO getFixProposal(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long fixProposalId) {
        return fixProposalService.getFixProposalByIdForUser(principal.getId(), fixProposalId);
    }
}
