package at.a11yforge.api.scanner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

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
        File tempFile = null;
        try {
            List<String> command = List.of("node", cliPath, url, String.join(",", rules));
            ProcessBuilder pb = new ProcessBuilder(command);
            tempFile = File.createTempFile("scanner-output", ".json");
            pb.redirectErrorStream(true);
            pb.redirectOutput(tempFile);

            Process process = pb.start();
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                throw new ScannerExecutionException("TIMEOUT" + timeoutSeconds + "s");
            } else {
                String output = Files.readString(tempFile.toPath());
                int exitCode = process.exitValue();
                if (exitCode != 0) {
                    throw new ScannerExecutionException("Scanner exit " + exitCode + ": " + output);
                }
                return objectMapper.readValue(output, PageScanResultDto.class);
            }
        } catch (IOException e) {
            throw new ScannerExecutionException("Scanner fehlgeschlagen", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ScannerExecutionException("Scanner-Prozess unterbrochen", e);
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }
}