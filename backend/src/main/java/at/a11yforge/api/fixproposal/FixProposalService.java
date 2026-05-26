package at.a11yforge.api.fixproposal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FixProposalService {

    private final FixProposalRepository fixProposalRepository;

    public FixProposalService(FixProposalRepository fixProposalRepository) {
        this.fixProposalRepository = fixProposalRepository;
    }

    @Transactional(readOnly = true)
    public List<FixProposalResponseDTO> getFixProposalsForUser(Long userId, Long violationId) {
        return fixProposalRepository.findAllByViolationIdAndViolationPageScanProjectUserId(violationId, userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FixProposalResponseDTO getFixProposalByIdForUser(Long userId, Long fixProposalId) {
        FixProposal fixProposal = fixProposalRepository.findByIdAndViolationPageScanProjectUserId(fixProposalId, userId)
                .orElseThrow(() -> new FixProposalNotFoundException(fixProposalId));
        return toResponse(fixProposal);
    }

    private FixProposalResponseDTO toResponse(FixProposal fixProposal) {
        return new FixProposalResponseDTO(
                fixProposal.getId(),
                fixProposal.getViolation().getId(),
                fixProposal.getStatus(),
                fixProposal.getGeneratedHtml(),
                fixProposal.getLlmProvider(),
                fixProposal.getLlmModel(),
                fixProposal.getPromptVersion()
        );
    }
}
