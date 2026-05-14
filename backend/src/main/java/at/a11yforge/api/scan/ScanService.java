package at.a11yforge.api.scan;

import org.springframework.stereotype.Service;

@Service
public class ScanService {

    private final ScanRepository scanRepository;

    public ScanService(ScanRepository scanRepository) {
        this.scanRepository = scanRepository;
    }
}
