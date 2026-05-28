package at.a11yforge.api.fixproposal;


import at.a11yforge.api.violation.Violation;
import jakarta.persistence.*;


@Entity
@Table(name = "fix_proposal")
public class FixProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "violation_id", nullable = false)
    private Violation violation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private FixProposalStatus status;

    @Column(name = "generated_html", columnDefinition = "TEXT")
    private String generatedHtml;

    @Column(name = "llm_provider", length = 50, nullable = false)
    private String llmProvider;

    @Column(name = "llm_model", length = 100)
    private String llmModel;

    @Column(name = "prompt_version", length = 20)
    private String promptVersion;

    protected FixProposal() {}

    public FixProposal(Violation violation, String llmProvider) {
        this.violation = violation;
        this.llmProvider = llmProvider;
        this.status = FixProposalStatus.PENDING;
    }

    public Long getId() {
        return id;
}

    public Violation getViolation() {
        return violation;
    }

    public FixProposalStatus getStatus() {
        return status;
    }

    public String getGeneratedHtml() {
        return generatedHtml;
    }

    public String getLlmProvider() {
        return llmProvider;
    }

    public String getLlmModel() {
        return llmModel;
    }

    public String getPromptVersion() {
        return promptVersion;
    }

    public void setStatus(FixProposalStatus status) {
        this.status = status;
    }

    public void setGeneratedHtml(String generatedHtml) {
        this.generatedHtml = generatedHtml;
    }

    public void setLlmModel(String llmModel) {
        this.llmModel = llmModel;
    }

    public void setPromptVersion(String promptVersion) {
        this.promptVersion = promptVersion;
    }

}
