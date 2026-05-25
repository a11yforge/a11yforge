package at.a11yforge.api.violation;

public class ViolationNotFoundException extends RuntimeException {

    public ViolationNotFoundException(Long violationId) {
        super("Violation with id " + violationId + " not found");
    }
}
