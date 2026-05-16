package at.a11yforge.api.scanner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ScannerProcessRunner {

    private final String cliPath;
    private final int timeoutSeconds;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScannerProcessRunner(
            @Value("${scanner.cli.path}") String cliPath,
            @Value("${scanner.timeout.seconds}") int timeoutSeconds
    ) {
        this.cliPath = cliPath;
        this.timeoutSeconds = timeoutSeconds;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public PageScanResultDto run(String url, List<String> rules) {

         List<String> command = List.of("node", cliPath, url, String.join(",", rules));
         ProcessBuilder pb = new ProcessBuilder(command);
         pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(output, PageScanResultDto.class);
        } catch (IOException e) {
            throw new ScannerExecutionException("Scanner fehlgeschlagen", e);
        }

}
}