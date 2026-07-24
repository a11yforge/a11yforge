package at.a11yforge.api.fixcache;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FixCacheService {

    private final FixCacheRepository fixCacheRepository;

    public FixCacheService(FixCacheRepository fixCacheRepository) {

      this.fixCacheRepository = fixCacheRepository;
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    @Transactional
    public Optional<String> findCachedFix(String ruleId, String htmlSnippet) {
        String violationHash = sha256(ruleId + "|" + htmlSnippet);
        return fixCacheRepository
            .findByViolationHash(violationHash)
            .map(
                cache -> {
                    cache.setHitCount(cache.getHitCount() + 1);
                    return cache.getCachedFixHtml();
                });
    }

    @Transactional
    public void cacheFix(String ruleId, String htmlSnippet, String fixHtml) {
        String violationHash = sha256(ruleId + "|" + htmlSnippet);
        if (fixCacheRepository.findByViolationHash(violationHash).isPresent()) {
            return;
        }
        String snippetHas = sha256(htmlSnippet);
        fixCacheRepository.save(new FixCache(violationHash, ruleId, snippetHas, fixHtml));
    }
}
