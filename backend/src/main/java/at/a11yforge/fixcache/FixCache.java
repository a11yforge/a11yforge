package at.a11yforge.fixcache;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "fix_cache",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_fix_cache_violation_hash",
                columnNames = "violation_hash"
        )
)
public class FixCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "violation_hash", nullable = false, length = 64)
    private String violationHash;

    @Column(name = "rule_id", length = 50, nullable = false)
    private String ruleId;

    @Column(name = "html_snippet_hash", length = 64, nullable = false)
    private String htmlSnippetHash;

    @Column(name = "cached_fix_html", columnDefinition = "TEXT", nullable = false)
    private String cachedFixHtml;

    @Column(name = "hit_count", nullable = false)
    private Integer hitCount = 0;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected FixCache() {}

    public FixCache(String violationHash, String ruleId, String htmlSnippetHash, String cachedFixHtml) {
        this.violationHash = violationHash;
        this.ruleId = ruleId;
        this.htmlSnippetHash = htmlSnippetHash;
        this.cachedFixHtml = cachedFixHtml;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getViolationHash() {
        return violationHash;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getHtmlSnippetHash() {
        return htmlSnippetHash;
    }

    public String getCachedFixHtml() {
        return cachedFixHtml;
    }

    public void setCachedFixHtml(String cachedFixHtml) {
        this.cachedFixHtml = cachedFixHtml;
    }

    public Integer getHitCount() {
        return hitCount;
    }

    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }


}