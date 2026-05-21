package at.a11yforge.api.verifier;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

            String json = objectMapper.writeValueAsString(request);
            process.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
            process.getOutputStream().close();

            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

            if(!finished) {
                process.destroyForcibly();
                throw new VerifierExecutionException(String.format("Process timed out after %d seconds.", timeoutSeconds));
            }

            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new VerifierExecutionException("Verifier exit " + exitCode + ": " + output);
            }
            return objectMapper.readValue(output, VerifyResultDTO.class);

        } catch (IOException e) {
            throw new VerifierExecutionException("Verifier failed", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new VerifierExecutionException("Verifier-Process break", e);
        }
    }



}
