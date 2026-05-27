package at.a11yforge.api.fixproposal;

import at.a11yforge.api.scanner.ViolationDto;
import at.a11yforge.api.verifier.VerifierProcessRunner;
import at.a11yforge.api.verifier.VerifyRequestDTO;
import at.a11yforge.api.verifier.VerifyResultDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixProposalServiceTest {

    @Test
    public void testFixProposal() {
        VerifierProcessRunner runner = new VerifierProcessRunner("../scanner/dist/cli-verify.js", 120);

        String originalHtml = "<html><body><img src=\"logo.png\"></body></html>";
        String oldSnippet  = "<img src=\"logo.png\">";
        String newSnippet  = "<img src=\"logo.png\" alt=\"Logo\">";

        ViolationDto target = new ViolationDto(
                "v1", "axe_violation", "image-alt", "critical",
                List.of(), "", "", "", List.of("img"), oldSnippet, null);

        VerifyRequestDTO request = new VerifyRequestDTO(
                originalHtml, oldSnippet, newSnippet,
                List.of(target), List.of("image-alt"), target);

        VerifyResultDTO result = runner.run(request);
        assertEquals("verified", result.status());
    }
}

