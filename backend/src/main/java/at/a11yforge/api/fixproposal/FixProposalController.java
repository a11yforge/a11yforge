package at.a11yforge.api.fixproposal;

import at.a11yforge.api.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/fix-proposals")
public class FixProposalController {

    private final FixProposalService fixProposalService;
    private final FixGenerationService fixGenerationService;

    public FixProposalController(FixProposalService fixProposalService,
                                 FixGenerationService fixGenerationService) {
        this.fixProposalService = fixProposalService;
        this.fixGenerationService = fixGenerationService;
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

    // HTTP 202 Accepted
    @PostMapping
    public ResponseEntity<FixGenerationStartResponseDTO> requestFixGeneration(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody @Valid FixGenerationRequestDTO request) {
        Long fixProposalId = fixGenerationService.requestGeneration(
                principal.getId(), request.violationId());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new FixGenerationStartResponseDTO(fixProposalId));
    }
}
