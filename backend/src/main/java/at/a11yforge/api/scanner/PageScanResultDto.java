package at.a11yforge.api.scanner;

import java.util.List;

public record PageScanResultDto(
        String scannerVersion,
        String url,
        String finalUrl,
        String scannedAt,
        long durationMs,
        String pageStatus,
        String failureReason,
        Integer httpStatus,
        String pageTitle,
        List<String> ruleSet,
        String renderedHtml,
        List<ViolationDto> violations,
        List<ViolationDto> incomplete
) {}