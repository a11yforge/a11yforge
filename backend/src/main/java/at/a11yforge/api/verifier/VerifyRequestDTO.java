package at.a11yforge.api.verifier;

import at.a11yforge.api.scanner.ViolationDto;

import java.util.List;

public record VerifyRequestDTO(
        String originalHtml,
        String oldSnippet,
        String newSnippet,
        List<ViolationDto> originalViolations,
        List<String> rules,
        ViolationDto targetViolation
) {}
