package at.a11yforge.api.llm;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PromptLoader {

    private static final String PROMPT_BASE_PATH = "prompts/";
    private static final String PROMPT_FILE_EXTENSION = ".txt";

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public String load(String promptName, String version) {
        String cacheKey = promptName + "-" + version;
        return cache.computeIfAbsent(cacheKey, key -> readFromClasspath(promptName, version));
    }

    public String fill(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() == null ? "" : entry.getValue();
            result = result.replace(placeholder, value);
        }
        return result;
    }

    private String readFromClasspath(String promptName, String version) {
        String path = PROMPT_BASE_PATH + promptName + "-" + version + PROMPT_FILE_EXTENSION;
        ClassPathResource resource = new ClassPathResource(path);
        try {
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PromptLoadException("Failed to load prompt: " + path, e);
        }
    }

    public String loadForRule(String promptName, String ruleId, String version) {
        String ruleSpecificName = promptName + "-" + ruleId;
        if (promptExists(ruleSpecificName, version)) {
            return load(ruleSpecificName, version);
        }
        return load(promptName, version);
    }

    private boolean promptExists(String promptName, String version) {
        String path = PROMPT_BASE_PATH + promptName + "-" + version + PROMPT_FILE_EXTENSION;
        return new ClassPathResource(path).exists();
    }
}
