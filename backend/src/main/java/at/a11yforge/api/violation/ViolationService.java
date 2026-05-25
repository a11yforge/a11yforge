package at.a11yforge.api.violation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViolationService {

    private final ViolationRepository violationRepository;

    public ViolationService(ViolationRepository violationRepository) {
        this.violationRepository = violationRepository;
    }

    @Transactional(readOnly = true)
    public List<ViolationResponseDTO> getViolationsForUser(Long userId, Long scanId) {
        return violationRepository.findAllByPageScanIdAndPageScanProjectUserId(scanId, userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ViolationResponseDTO getViolationByIdForUser(Long userId, Long violationId) {
        Violation violation = violationRepository.findByIdAndPageScanProjectUserId(violationId, userId)
                .orElseThrow(() -> new ViolationNotFoundException(violationId));
        return toResponse(violation);
    }

    private ViolationResponseDTO toResponse(Violation violation) {
        return new ViolationResponseDTO(
                violation.getId(),
                violation.getPage().getId(),
                violation.getRuleId(),
                violation.getSource(),
                violation.getImpact(),
                violation.getHtmlSnippet(),
                violation.getTargetSelector(),
                violation.getDescription()
        );
    }
}
