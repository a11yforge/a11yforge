package at.a11yforge.api.fixcache;

import org.springframework.stereotype.Service;

@Service
public class FixCacheService {

    private final FixCacheRepository fixCacheRepository;

    public FixCacheService(FixCacheRepository fixCacheRepository) {
        this.fixCacheRepository = fixCacheRepository;
    }
}
