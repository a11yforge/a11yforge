package at.a11yforge.api.fixproposal;

/**
 * Thrown when a fix is requested for a violation that is not fixable, e.g. an
 * axe "incomplete" (cantTell) finding, which is a needs-review hint rather than
 * a real violation and therefore has no meaningful LLM fix.
 */
public class ViolationNotFixableException extends RuntimeException {
  public ViolationNotFixableException(Long violationId) {
    super("Violation " + violationId + " is not fixable (axe incomplete / needs-review).");
  }
}
