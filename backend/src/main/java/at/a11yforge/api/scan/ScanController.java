package at.a11yforge.api.scan;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scan")
public class ScanController {

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }


    @PostMapping
    public ScanResponseDTO startScan(@RequestBody ScanResponseDTO dto) {
        return scanService.createAndRunScan(dto.projectId());
    }

    @GetMapping("/{id}")
    public ScanDetailDTO getScan(@PathVariable Long id) {
        return scanService.getScan(id);
    }
}
