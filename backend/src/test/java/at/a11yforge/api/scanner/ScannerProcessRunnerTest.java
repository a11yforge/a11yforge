package at.a11yforge.api.scanner;

import org.junit.jupiter.api.Test;
import java.util.List;

class ScannerProcessRunnerTest {

    @Test
    void scanReturnsResult() {
        ScannerProcessRunner runner = new ScannerProcessRunner("../scanner/dist/cli.js", 120);
        List<PageScanResultDto> result = runner.run("https://example.com", List.of("image-alt"), 1);
        System.out.println(result);
    }
}

