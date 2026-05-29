package at.a11yforge.api.llm;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FixTagExtractor {

    private static final Pattern FIX_TAG_PATTERN =
            Pattern.compile("<fix>(.*?)</fix>", Pattern.DOTALL);

    private FixTagExtractor() {}

    public static Optional<String> extract(String responseText) {
        if (responseText == null) {
            return Optional.empty();
        }
        Matcher matcher = FIX_TAG_PATTERN.matcher(responseText);
        if (matcher.find()) {
            return Optional.of(matcher.group(1).trim());
        }
        return Optional.empty();
    }
}
