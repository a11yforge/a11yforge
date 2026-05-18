package at.a11yforge.api.scan;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scan")
public class ScanController {

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }


    @PostMapping
    public ScanResponseDTO startScan(@RequestBody ScanCreateDTO dto) {
        return scanService.createAndRunScan(dto.projectId());
    }
}
