package at.a11yforge.fixproposal;


import at.a11yforge.violation.Violation;
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

    @Column(name = "generated_html", columnDefinition = "TEXT", nullable = false)
    private String generatedHtml;

    @Column(name = "llm_provider", length = 50, nullable = false)
    private String llmProvider;

    @Column(name = "llm_model", length = 100, nullable = false)
    private String llmModel;

    @Column(name = "prompt_version", length = 20, nullable = false)
    private String promptVersion;

    protected FixProposal() {}

    public FixProposal(Violation violation, String generatedHtml,
                       String llmProvider, String llmModel, String promptVersion) {
        this.violation = violation;
        this.generatedHtml = generatedHtml;
        this.llmProvider = llmProvider;
        this.llmModel = llmModel;
        this.promptVersion = promptVersion;
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

    public void setStatus(FixProposalStatus status) {
        this.status = status;
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



}
