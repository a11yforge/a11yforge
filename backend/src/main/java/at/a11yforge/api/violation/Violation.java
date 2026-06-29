package at.a11yforge.api.violation;

import at.a11yforge.api.page.Page;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "violation")
public class Violation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "page_id", nullable = false)
  private Page page;

  @Column(name = "rule_id", nullable = false, length = 50)
  private String ruleId;

  @Enumerated(EnumType.STRING)
  @Column(name = "source", nullable = false, length = 30)
  private ViolationSource source;

  @Enumerated(EnumType.STRING)
  @Column(name = "impact", nullable = false, length = 30)
  private Impact impact;

  @Column(name = "html_snippet", columnDefinition = "TEXT")
  private String htmlSnippet;

  @Column(name = "target_selector", length = 500)
  private String targetSelector;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "screenshot", columnDefinition = "TEXT")
  private String screenshot;

  @Column(name = "detected_lang")
  private String detectedLang;

  @Column(name = "fg_color", length = 30)
  private String fgColor;

  @Column(name = "bg_color", length = 30)
  private String bgColor;

  @Column(name = "contrast_ratio", precision = 5, scale = 2)
  private java.math.BigDecimal contrastRatio;

  @Column(name = "expected_contrast_ratio", length = 15)
  private String expectedContrastRatio;

  protected Violation() {}

  public Violation(Page page, String ruleId, ViolationSource source, Impact impact) {
    this.page = page;
    this.ruleId = ruleId;
    this.source = source;
    this.impact = impact;
    this.screenshot = screenshot;
  }

  public Long getId() {
    return id;
  }

  public Page getPage() {
    return page;
  }

  public String getRuleId() {
    return ruleId;
  }

  public ViolationSource getSource() {
    return source;
  }

  public Impact getImpact() {
    return impact;
  }

  public String getHtmlSnippet() {
    return htmlSnippet;
  }

  public void setHtmlSnippet(String htmlSnippet) {
    this.htmlSnippet = htmlSnippet;
  }

  public String getTargetSelector() {
    return targetSelector;
  }

  public void setTargetSelector(String targetSelector) {
    this.targetSelector = targetSelector;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getScreenshot() {
    return screenshot;
  }

  public void setScreenshot(String screenshot) {
    this.screenshot = screenshot;
  }

  public String getDetectedLang() {
    return detectedLang;
  }

  public void setDetectedLang(String detectedLang) {
    this.detectedLang = detectedLang;
  }

  public String getFgColor() {
    return fgColor;
  }

  public void setFgColor(String fgColor) {
    this.fgColor = fgColor;
  }

  public String getBgColor() {
    return bgColor;
  }

  public void setBgColor(String bgColor) {
    this.bgColor = bgColor;
  }

  public BigDecimal getContrastRatio() {
    return contrastRatio;
  }

  public void setContrastRatio(BigDecimal contrastRatio) {
    this.contrastRatio = contrastRatio;
  }

  public String getExpectedContrastRatio() {
    return expectedContrastRatio;
  }

  public void setExpectedContrastRatio(String expectedContrastRatio) {
    this.expectedContrastRatio = expectedContrastRatio;
  }

}
