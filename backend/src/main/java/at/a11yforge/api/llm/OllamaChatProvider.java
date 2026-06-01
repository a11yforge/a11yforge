package at.a11yforge.api.llm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class OllamaChatProvider implements ChatProvider {

    private static final String PROMPT_NAME = "fix-generation";

    private final RestClient restClient;
    private final PromptLoader promptLoader;
    private final String model;
    private final String promptVersion;

    public OllamaChatProvider(
            PromptLoader promptLoader,
            @Value("${a11yforge.llm.ollama.base-url}") String baseUrl,
            @Value("${a11yforge.llm.ollama.model}") String model,
            @Value("${a11yforge.llm.prompt-version}") String promptVersion
    ) {
        this.promptLoader = promptLoader;
        this.model = model;
        this.promptVersion = promptVersion;
        this.restClient = RestClient.create(baseUrl);
    }


    private String buildPrompt(FixGenerationRequestDTO request) {
        String template = promptLoader.load(PROMPT_NAME, promptVersion);
        Map<String, String> variables = new LinkedHashMap<>();
        variables.put("wcagRuleId", request.wcagRuleId());
        variables.put("impact", request.impact() == null ? "" : request.impact().name());
        variables.put("description", request.description());
        variables.put("targetSelector", request.targetSelector());
        variables.put("htmlSnippet", request.htmlSnippet());
        return promptLoader.fill(template, variables);
    }

    private FixGenerationResponseDTO failure(String reason) {
        return new FixGenerationResponseDTO(null, model, promptVersion, false, reason);
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.OLLAMA;
    }

    @Override
    public FixGenerationResponseDTO generateFix(FixGenerationRequestDTO request) {
        String prompt = buildPrompt(request);

        try {
            OllamaResponseDTO response = restClient.post()
                    .uri("/api/generate")
                    .body(new OllamaRequestDTO(model, prompt, false))
                    .retrieve()
                    .body(OllamaResponseDTO.class);

            if (response == null || response.response() == null) {
                return failure("Empty response from Ollama");
            }

            Optional<String> generatedHtml = FixTagExtractor.extract(response.response());
            if (generatedHtml.isEmpty()) {
                return failure("No <fix> tag found. Raw: " + response.response());
            }

            return new FixGenerationResponseDTO(generatedHtml.get(), model, promptVersion, true, null);

        } catch (RestClientException e) {
            return failure("Ollama call failed: " + e.getMessage());
        }
    }
}
