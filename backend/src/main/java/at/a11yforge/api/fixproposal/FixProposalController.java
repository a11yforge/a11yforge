package at.a11yforge.api.fixproposal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fix-proposals")
public class FixProposalController {

    private final FixProposalService fixProposalService;

    public FixProposalController(FixProposalService fixProposalService) {
        this.fixProposalService = fixProposalService;
    }
}
