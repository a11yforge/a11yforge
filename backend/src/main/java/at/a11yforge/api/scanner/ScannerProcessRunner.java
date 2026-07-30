package at.a11yforge.api.scanner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScannerProcessRunner {

  private static final Logger log = LoggerFactory.getLogger(ScannerProcessRunner.class);

  private final String cliPath;
  private final int timeoutSeconds;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public ScannerProcessRunner(
      @Value("${scanner.cli.path}") String cliPath,
      @Value("${scanner.timeout.seconds}") int timeoutSeconds) {
    this.cliPath = cliPath;
    this.timeoutSeconds = timeoutSeconds;
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private String normalizeUrl(String url) {
    String trimmed = url.trim();
    if (trimmed.contains("://")) {
      return trimmed;
    }
    return "https://" + trimmed;
  }

  private void assertPublicUrl(String url) {
    try {
      URI uri = new URI(url);

      String scheme = uri.getScheme();
      if (scheme == null || !(scheme.equals("http") || scheme.equals("https"))) {
        throw new UnsafeUrlException("Nur http/https erlaubt: " + url);
      }

      String host = uri.getHost();
      InetAddress adresse = InetAddress.getByName(host);
      if (adresse.isLoopbackAddress()
          || adresse.isSiteLocalAddress()
          || adresse.isLinkLocalAddress()
          || adresse.isAnyLocalAddress()) {
        throw new UnsafeUrlException("URL zeigt auf eine interne Adresse: " + host);
      }
    } catch (URISyntaxException | UnknownHostException e) {
      throw new UnsafeUrlException("Ungültige oder nicht auflösbare URL: " + url, e);
    }
  }

  public List<PageScanResultDto> run(String url, List<String> rules, int maxPages) {
    url = normalizeUrl(url);
    assertPublicUrl(url);
    File tempFile = null;
    try {
      List<String> command =
          List.of("node", cliPath, url, String.join(",", rules), String.valueOf(maxPages));
      ProcessBuilder pb = new ProcessBuilder(command);
      tempFile = File.createTempFile("scanner-output", ".json");
      pb.redirectErrorStream(true);
      pb.redirectOutput(tempFile);

      Process process = pb.start();
      boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
      if (!finished) {
        process.destroyForcibly();
        log.error("Scanner timeout nach {}s für url={}", timeoutSeconds, url);
        throw new ScannerExecutionException("TIMEOUT" + timeoutSeconds + "s");
      }

      String output = Files.readString(tempFile.toPath());
      int exitCode = process.exitValue();
      if (exitCode != 0) {
        log.error("Scanner exit {} für url={}: {}", exitCode, url, output);
        throw new ScannerExecutionException("Scanner exit " + exitCode + ": " + output);
      }

      log.debug("Scanner output für url={}: {}", url, output);

      PageScanResultDto[] resultsArray = objectMapper.readValue(output, PageScanResultDto[].class);
      return List.of(resultsArray);

    } catch (IOException e) {
      log.error("Scanner fehlgeschlagen für url={}", url, e);
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
