package at.a11yforge.api.scan;

public class ScanNotFoundException extends RuntimeException {
    public ScanNotFoundException(Long scanId) {super("Scan not found: " + scanId);}
}
