package at.a11yforge.scan;


import at.a11yforge.project.Project;
import jakarta.persistence.*;
import java.time.Instant;

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

  protected Scan() {}

  public Scan(Project project) {
    this.project = project;
    this.startedAt = Instant.now();
    this.status = ScanStatus.RUNNING;
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

  public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
  }


}