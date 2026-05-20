package at.a11yforge.api.verifier;

public class VerifierExecutionException extends RuntimeException {

    public VerifierExecutionException(String message) {
        super(message);
    }
    public VerifierExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
