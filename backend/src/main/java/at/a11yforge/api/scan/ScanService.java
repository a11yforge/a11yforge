package at.a11yforge.api.scan;

import at.a11yforge.api.auditevent.AuditEventService;
import at.a11yforge.api.auditevent.AuditEventType;
import at.a11yforge.api.llm.ProviderType;
import at.a11yforge.api.page.Page;
import at.a11yforge.api.page.PageRepository;
import at.a11yforge.api.project.Project;
import at.a11yforge.api.project.ProjectNotFoundException;
import at.a11yforge.api.project.ProjectRepository;
import at.a11yforge.api.scanner.PageScanResultDto;
import at.a11yforge.api.scanner.ScannerExecutionException;
import at.a11yforge.api.scanner.ScannerProcessRunner;
import at.a11yforge.api.scanner.ViolationDto;
import at.a11yforge.api.violation.Impact;
import at.a11yforge.api.violation.Violation;
import at.a11yforge.api.violation.ViolationRepository;
import at.a11yforge.api.violation.ViolationResponseDTO;
import at.a11yforge.api.violation.ViolationSource;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ScanService {

  private static final Logger log = LoggerFactory.getLogger(ScanService.class);

  private final ScanRepository scanRepository;
  private final PageRepository pageRepository;
  private final ViolationRepository violationRepository;
  private final ProjectRepository projectRepository;
  private final ScannerProcessRunner scanner;
  private final AuditEventService auditEventService;

  public ScanService(
      ScanRepository scanRepository,
      PageRepository pageRepository,
      ViolationRepository violationRepository,
      ProjectRepository projectRepository,
      ScannerProcessRunner scanner,
      AuditEventService auditEventService) {
    this.scanRepository = scanRepository;
    this.pageRepository = pageRepository;
    this.violationRepository = violationRepository;
    this.projectRepository = projectRepository;
    this.scanner = scanner;
    this.auditEventService = auditEventService;
  }

  public ScanResponseDTO createAndRunScan(Long userId, Long projectId, ProviderType llmProvider) {
    Project project =
        projectRepository
            .findByIdAndUserId(projectId, userId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));
    Scan scan = scanRepository.save(new Scan(project, llmProvider));

    try {

      List<String> rules =
          List.of("image-alt", "color-contrast", "label", "html-has-lang", "heading-order");
      List<PageScanResultDto> results =
          scanner.run(project.getBaseUrl(), rules, project.getCrawlMaxPages());

      for (PageScanResultDto result : results) {
        Page page = new Page(scan, result.finalUrl());
        page.setHttpStatus(result.httpStatus());
        page.setRenderedHtml(result.renderedHtml());
        page = pageRepository.save(page);

        for (ViolationDto v : result.violations()) {
          Violation violation =
              new Violation(
                  page,
                  v.ruleId(),
                  ViolationSource.valueOf(v.source().toUpperCase()),
                  Impact.valueOf(v.impact().toUpperCase()));
          violation.setHtmlSnippet(v.htmlSnippet());
          violation.setDescription(v.description());
          // Annahme: einteilige Adresse, mehrteilige sind out of scope
          violation.setTargetSelector(v.target().isEmpty() ? null : v.target().get(0));
          violationRepository.save(violation);
        }

        for (ViolationDto v : result.incomplete()) {
          Violation violation =
              new Violation(
                  page,
                  v.ruleId(),
                  ViolationSource.valueOf(v.source().toUpperCase()),
                  Impact.valueOf(v.impact().toUpperCase()));
          violation.setHtmlSnippet(v.htmlSnippet());
          violation.setDescription(v.description());
          violation.setTargetSelector(v.target().isEmpty() ? null : v.target().get(0));
          violationRepository.save(violation);
        }
      }

      scan.setStatus(ScanStatus.COMPLETED);
      scan.setCompletedAt(Instant.now());
      scan = scanRepository.save(scan);

      auditEventService.recordEvent(
          userId, "Scan", scan.getId(), AuditEventType.CREATED, null, scan.getStatus().name());

      return new ScanResponseDTO(
          scan.getId(),
          scan.getProject().getId(),
          scan.getStatus().name(),
          scan.getStartedAt(),
          scan.getCompletedAt());

    } catch (ScannerExecutionException e) {
      scan.setStatus(ScanStatus.FAILED);
      scan.setErrorMessage(e.getMessage());
      scan.setCompletedAt(Instant.now());
      scanRepository.save(scan);
      log.error("Scan {} fehlgeschlagen", scan.getId(), e);
      throw e;
    }
  }

  public ScanDetailDTO getScan(Long scanId, Long userId) {
    Scan scan =
        scanRepository
            .findByIdAndProjectUserId(scanId, userId)
            .orElseThrow(() -> new ScanNotFoundException(scanId));

    List<ViolationResponseDTO> violations =
        violationRepository.findByPage_Scan_Id(scanId).stream()
            .map(
                v ->
                    new ViolationResponseDTO(
                        v.getId(),
                        v.getPage().getId(),
                        v.getRuleId(),
                        v.getSource(),
                        v.getImpact(),
                        v.getHtmlSnippet(),
                        v.getTargetSelector(),
                        v.getDescription()))
            .toList();

    return new ScanDetailDTO(
        scan.getId(),
        scan.getProject().getId(),
        scan.getStatus().name(),
        scan.getStartedAt(),
        scan.getCompletedAt(),
        violations);
  }
}
