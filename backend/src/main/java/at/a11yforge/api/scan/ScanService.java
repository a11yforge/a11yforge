package at.a11yforge.api.scan;

import at.a11yforge.api.page.PageRepository;
import at.a11yforge.api.project.ProjectRepository;
import at.a11yforge.api.scanner.ScannerProcessRunner;
import at.a11yforge.api.violation.ViolationRepository;
import org.springframework.stereotype.Service;

@Service
public class ScanService {

    private final ScanRepository scanRepository;
    private final PageRepository pageRepository;
    private final ViolationRepository violationRepository;
    private final ProjectRepository projectRepository;
    private final ScannerProcessRunner scanner;

    public ScanService(ScanRepository scanRepository, PageRepository pageRepository, ViolationRepository violationRepository, ProjectRepository projectRepository, ScannerProcessRunner scanner) {

        this.scanRepository = scanRepository;
        this.pageRepository = pageRepository;
        this.violationRepository = violationRepository;
        this.projectRepository = projectRepository;
        this.scanner = scanner;
    }
}
