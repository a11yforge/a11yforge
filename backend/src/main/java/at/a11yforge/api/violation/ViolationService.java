package at.a11yforge.api.violation;

import org.springframework.stereotype.Service;

@Service
public class ViolationService {

    private final ViolationRepository violationRepository;

    public ViolationService(ViolationRepository violationRepository) {
        this.violationRepository = violationRepository;
    }
}
