package at.a11yforge.api.scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScannerProcessRunner {

    private final String cliPath;
    private final int timeoutSeconds;

    public ScannerProcessRunner(
            @Value("${scanner.cli.path}") String cliPath,
            @Value("${scanner.timeout.seconds}") int timeoutSeconds
    ) {
        this.cliPath = cliPath;
        this.timeoutSeconds = timeoutSeconds;
    }
}