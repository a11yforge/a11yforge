package at.a11yforge.api.violation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/violations")
public class ViolationController {

    private final ViolationService violationService;

    public ViolationController(ViolationService violationService) {
        this.violationService = violationService;
    }
}
