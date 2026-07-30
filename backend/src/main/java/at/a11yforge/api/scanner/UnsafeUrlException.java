package at.a11yforge.api.scanner;

public class UnsafeUrlException extends RuntimeException {
  public UnsafeUrlException(String message) {
    super(message);
  }

  public UnsafeUrlException(String message, Throwable cause) {
    super(message, cause);
  }
}
