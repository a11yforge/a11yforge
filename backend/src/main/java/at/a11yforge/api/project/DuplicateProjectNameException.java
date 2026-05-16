package at.a11yforge.api.project;

public class DuplicateProjectNameException extends RuntimeException {
  public DuplicateProjectNameException(String message) {
    super(message);
  }
}
