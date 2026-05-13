package at.a11yforge.violation;

import jakarta.persistence.*;
import at.a11yforge.page.Page;


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

    protected Violation() {}

    public Violation(Page page, String ruleId, ViolationSource source, Impact impact) {
        this.page = page;
        this.ruleId = ruleId;
        this.source = source;
        this.impact = impact;
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
}
