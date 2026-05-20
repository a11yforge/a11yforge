package at.a11yforge.api.verifier;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class VerifierProcessRunner {
    private final String cliPath;
    private final int timeoutSeconds;
    private final ObjectMapper objectMapper =
            new ObjectMapper();

    public VerifierProcessRunner(
            @Value("${verifier.cli.path}")String cliPath,  @Value("${verifier.timeout.seconds}") int timeoutSeconds)
    {
        this.cliPath = cliPath;
        this.timeoutSeconds = timeoutSeconds;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public VerifyResultDTO run(VerifyRequestDTO request) {
        List<String> command = List.of("node", cliPath);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        try{
            Process process = pb.start();
        } catch (IOException e) {
            throw new VerifierExecutionException("Failed to start process", e);
        }
        throw new UnsupportedOperationException("not yet implemented");
    }



}
