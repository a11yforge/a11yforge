package at.a11yforge.api.scan;

import at.a11yforge.api.auditevent.AuditEventService;
import at.a11yforge.api.auditevent.AuditEventType;
import at.a11yforge.api.fixproposal.FixProposal;
import at.a11yforge.api.fixproposal.FixProposalRepository;
import at.a11yforge.api.fixproposal.FixProposalStatus;
import at.a11yforge.api.llm.ChatProviderFactory;
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
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScanService {

  private static final Logger log = LoggerFactory.getLogger(ScanService.class);

  private final ProjectRepository projectRepository;
  private final ScanRepository scanRepository;
  private final PageRepository pageRepository;
  private final ViolationRepository violationRepository;
  private final ReviewRepository reviewRepository;
  private final ScannerProcessRunner scannerProcessRunner;
  private final AuditEventService auditEventService;
  private final ChatProviderFactory chatProviderFactory;
  private final FixProposalRepository fixProposalRepository;

  private final Executor executor = Executors.newCachedThreadPool();

  @Value("${a11yforge.llm.default-provider}")
  private ProviderType defaultProvider;

  public ScanService(
      ProjectRepository projectRepository,
      ScanRepository scanRepository,
      PageRepository pageRepository,
      ViolationRepository violationRepository,
      ReviewRepository reviewRepository,
      ScannerProcessRunner scannerProcessRunner,
      AuditEventService auditEventService,
      ChatProviderFactory chatProviderFactory,
      FixProposalRepository fixProposalRepository) {
    this.projectRepository = projectRepository;
    this.scanRepository = scanRepository;
    this.pageRepository = pageRepository;
    this.violationRepository = violationRepository;
    this.reviewRepository = reviewRepository;
    this.scannerProcessRunner = scannerProcessRunner;
    this.auditEventService = auditEventService;
    this.chatProviderFactory = chatProviderFactory;
    this.fixProposalRepository = fixProposalRepository;
  }

  public ScanResponseDTO startScan(Long userId, Long projectId) {
    Project project =
        projectRepository
            .findByIdAndUserId(projectId, userId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

    Scan scan = scanRepository.save(new Scan(project, defaultProvider));

    executor.execute(() -> runFullScan(scan, project, userId));

    return toResponse(scan);
  }

  public ScanResponseDTO createAndRunScan(Long userId, Long projectId) {
    Project project =
        projectRepository
            .findByIdAndUserId(projectId, userId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

    Scan scan = scanRepository.save(new Scan(project, defaultProvider));

    runFullScan(scan, project, userId);

    return toResponse(scan);
  }

  @Transactional(readOnly = true)
  public ScanDetailDTO getScan(Long userId, Long scanId) {

    Scan scan =
        scanRepository
            .findByIdAndProjectUserId(scanId, userId)
            .orElseThrow(() -> new ScanNotFoundException(scanId));

    long displayNumber =
        scanRepository.countByProjectIdAndIdLessThanEqual(scan.getProject().getId(), scan.getId());

    List<ViolationResponseDTO> violations =
        violationRepository.findByPage_Scan_Id(scanId).stream()
            .filter(v -> v.getSource() != ViolationSource.AXE_INCOMPLETE)
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

    Map<Long, ReviewDecision> decisionByProposalId =
        reviewRepository.findByFixProposal_Violation_Page_Scan_Id(scanId).stream()
            .collect(
                Collectors.toMap(
                    r -> r.getFixProposal().getId(),
                    Function.identity(),
                    (a, b) -> a.getId() >= b.getId() ? a : b))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getReviewDecision()));

    Map<Long, FixProposal> latestProposalByViolationId =
        fixProposalRepository.findAllByViolationPageScanId(scanId).stream()
            .collect(
                Collectors.toMap(
                    fp -> fp.getViolation().getId(), Function.identity(), BinaryOperatorLatest()));

    List<ViolationFixDTO> fixes =
        latestProposalByViolationId.values().stream()
            .map(
                fp ->
                    new ViolationFixDTO(
                        fp.getId(),
                        fp.getViolation().getId(),
                        fp.getStatus().name(),
                        fp.getGeneratedHtml(),
                        fp.getLlmProvider(),
                        fp.getLlmModel(),
                        fp.getPromptVersion(),
                        decisionByProposalId.get(fp.getId())))
            .toList();

    return new ScanDetailDTO(
        scan.getId(),
        scan.getProject().getId(),
        scan.getStatus().name(),
        scan.getStartedAt(),
        scan.getCompletedAt(),
        violations,
        fixes,
        displayNumber);
  }

  public List<ScanResponseDTO> getScansForProject(Long userId, Long projectId) {
    return scanRepository
        .findAllByProjectIdAndProjectUserIdOrderByIdDesc(projectId, userId)
        .stream()
        .map(
            s ->
                new ScanResponseDTO(
                    s.getId(),
                    s.getProject().getId(),
                    s.getStatus().name(),
                    s.getStartedAt(),
                    s.getCompletedAt(),
                    violationRepository.countByPage_Scan_IdAndSourceNot(
                        s.getId(), ViolationSource.AXE_INCOMPLETE),
                    scanRepository.countByProjectIdAndIdLessThanEqual(
                        s.getProject().getId(), s.getId())))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ExportDTO> exportReviewedFixes(Long userId, Long scanId) {
    scanRepository
        .findByIdAndProjectUserId(scanId, userId)
        .orElseThrow(() -> new ScanNotFoundException(scanId)); // Owner-Gate
    return reviewRepository.findByFixProposal_Violation_Page_Scan_Id(scanId).stream()
        .collect(
            Collectors.toMap(
                r -> r.getFixProposal().getId(),
                Function.identity(),
                (a, b) -> a.getId() >= b.getId() ? a : b))
        .values()
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
                  fp.getGeneratedHtml(),
                  v.getImpact(),
                  v.getDescription(),
                  fp.getStatus() == FixProposalStatus.VERIFIED,
                  r.getReviewDecision());
            })
        .toList();
  }

  private void runFullScan(Scan scan, Project project, Long userId) {
    try {
      List<String> rules =
          List.of("image-alt", "color-contrast", "label", "html-has-lang", "heading-order");
      List<PageScanResultDto> results =
          scannerProcessRunner.run(project.getBaseUrl(), rules, project.getCrawlMaxPages());

      for (PageScanResultDto result : results) {
        Page page = new Page(scan, result.finalUrl());
        page.setHttpStatus(result.httpStatus());
        page.setRenderedHtml(result.renderedHtml());
        page = pageRepository.save(page);

        persistViolations(page, result.violations(), true);
        persistViolations(page, result.incomplete(), false);
      }

      scan.setStatus(ScanStatus.COMPLETED);
      scan.setCompletedAt(Instant.now());
      scanRepository.save(scan);

      auditEventService.recordEvent(
          userId, "Scan", scan.getId(), AuditEventType.CREATED, null, scan.getStatus().name());

    } catch (ScannerExecutionException e) {
      scan.setStatus(ScanStatus.FAILED);
      scan.setErrorMessage(e.getMessage());
      scan.setCompletedAt(Instant.now());
      scanRepository.save(scan);
      log.error("Scan {} fehlgeschlagen", scan.getId(), e);
    }
  }

  private void persistViolations(Page page, List<ViolationDto> dtos, boolean withScreenshot) {
    for (ViolationDto v : dtos) {
      Violation violation =
          new Violation(
              page,
              v.ruleId(),
              ViolationSource.valueOf(v.source().toUpperCase()),
              Impact.valueOf(v.impact().toUpperCase()));
      violation.setHtmlSnippet(v.htmlSnippet());
      violation.setDescription(v.description());
      violation.setTargetSelector(v.target().isEmpty() ? null : v.target().get(0));

      String lang = v.detectedLang();
      if (lang == null && v.langSample() != null) {
        lang = chatProviderFactory.getProvider(defaultProvider).detectLanguage(v.langSample());
      }
      violation.setDetectedLang(lang);

      if (withScreenshot) {
        violation.setScreenshot(v.screenshot());
      }

      if ("color-contrast".equals(v.ruleId())) {
        violation.setFgColor(v.fgColor());
        violation.setBgColor(v.bgColor());
        violation.setContrastRatio(v.contrastRatio());
        violation.setExpectedContrastRatio(v.expectedContrastRatio());
      }

      violationRepository.save(violation);
    }
  }

  private ScanResponseDTO toResponse(Scan scan) {
    long displayNumber =
        scanRepository.countByProjectIdAndIdLessThanEqual(scan.getProject().getId(), scan.getId());

    return new ScanResponseDTO(
        scan.getId(),
        scan.getProject().getId(),
        scan.getStatus().name(),
        scan.getStartedAt(),
        scan.getCompletedAt(),
        violationRepository.countByPage_Scan_IdAndSourceNot(
            scan.getId(), ViolationSource.AXE_INCOMPLETE),
        displayNumber);
  }

  private static java.util.function.BinaryOperator<FixProposal> BinaryOperatorLatest() {
    return (a, b) -> a.getId() >= b.getId() ? a : b;
  }
}
