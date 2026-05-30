package at.a11yforge.api.scan;


import at.a11yforge.api.project.Project;
import jakarta.persistence.*;
import java.time.Instant;
import at.a11yforge.api.llm.ProviderType;

@Entity
@Table(name = "scan")
public class Scan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private ScanStatus status;

  @Column(name = "started_at", nullable = false)
  private Instant startedAt;

  @Column(name = "completed_at")
  private Instant completedAt;

  @Column(name = "error_message", columnDefinition = "TEXT")
  private String errorMessage;

  @Enumerated(EnumType.STRING)
  @Column(name = "llm_provider", nullable = false, length = 20)
  private ProviderType llmProvider;

  protected Scan() {}

  // TODO Muss entfernt werden Aufruf muss erweitert werden
  public Scan(Project project) {
    this.project = project;
    this.startedAt = Instant.now();
    this.status = ScanStatus.RUNNING;
  }

  public Scan(Project project, ProviderType llmProvider) {
    this.project = project;
    this.startedAt = Instant.now();
    this.status = ScanStatus.RUNNING;
    this.llmProvider = llmProvider;
  }

  public Long getId() {
    return id;
  }

  public Project getProject() {
    return project;
  }

  public ScanStatus getStatus() {
      return status;
  }

  public void setStatus(ScanStatus status) {
      this.status = status;
  }

  public Instant getStartedAt() {
      return startedAt;
  }

  public Instant getCompletedAt() {
      return completedAt;
  }

  public void setCompletedAt(Instant completedAt) {
      this.completedAt = completedAt;
  }

  public String getErrorMessage() {
      return errorMessage;
  }

  public ProviderType getLlmProvider() {
  return llmProvider;
  }

  public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
  }


}