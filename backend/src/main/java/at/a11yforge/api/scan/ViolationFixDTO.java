package at.a11yforge.api.scan;

import at.a11yforge.api.review.ReviewDecision;

public record ViolationFixDTO(
  Long id,
  Long violationId,
  String status,
  String generatedHtml,
  String llmProvider,
  String llmModel,
  String promptVersion,
  ReviewDecision decision) {}
