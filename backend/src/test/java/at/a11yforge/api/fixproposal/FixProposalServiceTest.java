package at.a11yforge.api.fixproposal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import at.a11yforge.api.llm.NoOpChatProvider;
import at.a11yforge.api.page.Page;
import at.a11yforge.api.scanner.ViolationDto;
import at.a11yforge.api.verifier.VerifierProcessRunner;
import at.a11yforge.api.verifier.VerifyRequestDTO;
import at.a11yforge.api.verifier.VerifyResultDTO;
import at.a11yforge.api.violation.Impact;
import at.a11yforge.api.violation.Violation;
import at.a11yforge.api.violation.ViolationSource;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FixProposalServiceTest {

  @Mock FixProposalRepository fixProposalRepository;
  @Mock VerifierProcessRunner verifierProcessRunner;
  @InjectMocks FixProposalService fixProposalService;

  @Test
  public void verifiedFixSaved() {
    when(verifierProcessRunner.run(any())).thenReturn(new VerifyResultDTO("verified", null));
    when(fixProposalRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    Violation violation =
        new Violation(
            mock(Page.class), "image-alt", ViolationSource.AXE_VIOLATION, Impact.CRITICAL);

    ViolationDto target =
      new ViolationDto(
        "v1",
        "axe_violation",
        "image-alt",
        "critical",
        List.of(),
        "",
        "",
        "",
        List.of("img"),
        "<img>",
        null,
        null,
        null,
        null,
        null,
        null,
        null);
        new ViolationDto(
            "v1",
            "axe_violation",
            "image-alt",
            "critical",
            List.of(),
            "",
            "",
            "",
            List.of("img"),
            "<img>",
            null,
            null,
            null,
            null);
    //

    FixProposal result =
        fixProposalService.generateAndVerify(
            violation,
            "<html>",
            List.of(target),
            List.of("image-alt"),
            new NoOpChatProvider(),
            target);

    assertEquals(FixProposalStatus.VERIFIED, result.getStatus());
  }

  @Test
  public void testFixProposal() {

    VerifierProcessRunner runner = new VerifierProcessRunner("../scanner/dist/cli-verify.js", 120);

    String originalHtml = "<html><body><img src=\"logo.png\"></body></html>";
    String oldSnippet = "<img src=\"logo.png\">";
    String newSnippet = "<img src=\"logo.png\" alt=\"Logo\">";

    ViolationDto target =
      new ViolationDto(
        "v1",
        "axe_violation",
        "image-alt",
        "critical",
        List.of(),
        "",
        "",
        "",
        List.of("img"),
        "<img>",
        null,
        null,
        null,
        null,
        null,
        null,
        null);
        new ViolationDto(
            "v1",
            "axe_violation",
            "image-alt",
            "critical",
            List.of(),
            "",
            "",
            "",
            List.of("img"),
            "<img>",
            null,
            null,
            null,
            null);

    VerifyRequestDTO request =
        new VerifyRequestDTO(
            originalHtml, oldSnippet, newSnippet, List.of(target), List.of("image-alt"), target);

    VerifyResultDTO result = runner.run(request);
    assertEquals("verified", result.status());
  }

  @Test
  public void badFixDiscarded() {
    VerifierProcessRunner runner = new VerifierProcessRunner("../scanner/dist/cli-verify.js", 120);

    String originalHtml = "<html><body><img src=\"logo.png\"></body></html>";
    String oldSnippet = "<img src=\"logo.png\">";
    String newSnippet = "<img src=\"logo.png\" class=\"x\">";

    ViolationDto target =
      new ViolationDto(
        "v1",
        "axe_violation",
        "image-alt",
        "critical",
        List.of(),
        "",
        "",
        "",
        List.of("img"),
        "<img>",
        null,
        null,
        null,
        null,
        null,
        null,
        null);
        new ViolationDto(
            "v1",
            "axe_violation",
            "image-alt",
            "critical",
            List.of(),
            "",
            "",
            "",
            List.of("img"),
            "<img>",
            null,
            null,
            null,
            null);

    VerifyRequestDTO request =
        new VerifyRequestDTO(
            originalHtml, oldSnippet, newSnippet, List.of(target), List.of("image-alt"), target);

    VerifyResultDTO result = runner.run(request);
    assertEquals("discarded", result.status());
  }
}
