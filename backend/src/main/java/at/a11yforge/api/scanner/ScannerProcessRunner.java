package at.a11yforge.api.scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    public PageScanResultDto run(String url, List<String> rules) {

         List<String> command = List.of("node", cliPath, url, String.join(",", rules));
         ProcessBuilder pb = new ProcessBuilder(command);
         pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return null;
        } catch (IOException e) {
            throw new ScannerExecutionException("Scanner fehlgeschlagen", e);
        }

}
}