package at.a11yforge.api.scan;

import at.a11yforge.api.auditevent.AuditEventService;
import at.a11yforge.api.auditevent.AuditEventType;
import at.a11yforge.api.llm.ProviderType;
import at.a11yforge.api.page.Page;
import at.a11yforge.api.page.PageRepository;
import at.a11yforge.api.project.Project;
import at.a11yforge.api.project.ProjectNotFoundException;
import at.a11yforge.api.project.ProjectRepository;
import at.a11yforge.api.review.ReviewDecision;
import at.a11yforge.api.review.ReviewRepository;
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
import org.springframework.beans.factory.annotation.Value;
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
  private final ReviewRepository reviewRepository;

  @Value("${a11yforge.llm.default-provider}")
  private ProviderType defaultProvider;

  public ScanService(
      ScanRepository scanRepository,
      PageRepository pageRepository,
      ViolationRepository violationRepository,
      ProjectRepository projectRepository,
      ScannerProcessRunner scanner,
      AuditEventService auditEventService,
      ReviewRepository reviewRepository) {
    this.scanRepository = scanRepository;
    this.pageRepository = pageRepository;
    this.violationRepository = violationRepository;
    this.projectRepository = projectRepository;
    this.scanner = scanner;
    this.auditEventService = auditEventService;
    this.reviewRepository = reviewRepository;
  }

  public ScanResponseDTO createAndRunScan(Long userId, Long projectId) {
    Project project =
        projectRepository
            .findByIdAndUserId(projectId, userId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));
    Scan scan = scanRepository.save(new Scan(project, defaultProvider));

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
          violation.setTargetSelector(v.target().isEmpty() ? null : v.target().get(0));
          violation.setScreenshot(v.screenshot());
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
          scan.getCompletedAt(),
          violationRepository.countByPage_Scan_Id(scan.getId()));

    } catch (ScannerExecutionException e) {
      scan.setStatus(ScanStatus.FAILED);
      scan.setErrorMessage(e.getMessage());
      scan.setCompletedAt(Instant.now());
      scanRepository.save(scan);
      log.error("Scan {} fehlgeschlagen", scan.getId(), e);
      throw e;
    }
  }

  public ScanDetailDTO getScan(Long userId, Long scanId) {
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

  public List<ScanResponseDTO> getScansForProject(Long userId, Long projectId) {
    return scanRepository.findAllByProjectIdAndProjectUserId(projectId, userId).stream()
        .map(
            s ->
                new ScanResponseDTO(
                    s.getId(),
                    s.getProject().getId(),
                    s.getStatus().name(),
                    s.getStartedAt(),
                    s.getCompletedAt(),
                    violationRepository.countByPage_Scan_Id(s.getId())))
        .toList();
  }

  public List<ExportDTO> exportAcceptedFixes(Long userId, Long scanId) {
    scanRepository
        .findByIdAndProjectUserId(scanId, userId)
        .orElseThrow(() -> new ScanNotFoundException(scanId)); // Owner-Gate
    return reviewRepository
        .findByReviewDecisionAndFixProposal_Violation_Page_Scan_Id(ReviewDecision.ACCEPTED, scanId)
        .stream()
        .map(
            r -> {
              var fp = r.getFixProposal();
              var v = fp.getViolation();
              return new ExportDTO(
                  v.getId(),
                  v.getRuleId(),
                  v.getTargetSelector(),
                  v.getHtmlSnippet(),
                  fp.getGeneratedHtml());
            })
        .toList();
  }
}
