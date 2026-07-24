package at.a11yforge.api.llm;

import at.a11yforge.api.violation.Impact;

import java.math.BigDecimal;

public record FixGenerationRequestDTO(
      String htmlSnippet,
      String wcagRuleId,
      String targetSelector,
      String description,
      Impact impact,
      String screenshot,
      String fgColor,
      String bgColor,
      BigDecimal contrastRatio,
      String expectedContrastRatio,
      String language
) {

}
