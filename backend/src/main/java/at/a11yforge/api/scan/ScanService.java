package at.a11yforge.api.scan;

import at.a11yforge.api.page.PageRepository;
import at.a11yforge.api.project.Project;
import at.a11yforge.api.project.ProjectNotFoundException;
import at.a11yforge.api.project.ProjectRepository;
import at.a11yforge.api.scanner.PageScanResultDto;
import at.a11yforge.api.scanner.ScannerProcessRunner;
import at.a11yforge.api.violation.ViolationRepository;
import org.springframework.stereotype.Service;
import at.a11yforge.api.page.Page;

import java.util.List;

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

    public ScanResponseDTO createAndRunScan(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        Scan scan = scanRepository.save(new Scan(project));


        List<String> rules = List.of("image-alt", "color-contrast", "label", "html-has-lang", "heading-order");
        PageScanResultDto result = scanner.run(project.getBaseUrl(), rules);

        Page page = new Page(scan, result.finalUrl());
        page.setHttpStatus(result.httpStatus());
        page.setRenderedHtml(result.renderedHtml());
        page = pageRepository.save(page);
            return null;
    }
}
