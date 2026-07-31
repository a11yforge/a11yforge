package at.a11yforge.api.ratelimit;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

  private final Map<String, Window> windows = new ConcurrentHashMap<>();

  record Window(Instant start, int count) {}

  public void check(String key, int maxPerHour) {
    Instant now = Instant.now();
    windows.compute(
        key,
        (k, window) -> {
          if (window == null || now.isAfter(window.start().plus(Duration.ofHours(1)))) {
            return new Window(now, 1);
          }
          if (window.count() >= maxPerHour) {
            throw new RateLimitExceededException(
                "Zu viele Anfragen, bitte spaeter erneut versuchen");
          }
          return new Window(window.start(), window.count() + 1);
        });
  }
}
