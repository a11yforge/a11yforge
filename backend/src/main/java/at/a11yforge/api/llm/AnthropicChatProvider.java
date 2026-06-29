package at.a11yforge.api.llm;

import com.anthropic.client.AnthropicClient;
import com.anthropic.errors.AnthropicException;
import com.anthropic.models.messages.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AnthropicChatProvider implements ChatProvider {

  private static final String PROMPT_NAME = "fix-generation";
  private static final long MAX_TOKENS = 2048L;

  private final AnthropicClient client;
  private final PromptLoader promptLoader;
  private final String model;
  private final String promptVersion;

  public AnthropicChatProvider(
      AnthropicClient client,
      PromptLoader promptLoader,
      @Value("${a11yforge.llm.anthropic.model}") String model,
      @Value("${a11yforge.llm.prompt-version}") String promptVersion) {
    this.client = client;
    this.promptLoader = promptLoader;
    this.model = model;
    this.promptVersion = promptVersion;
  }

  @Override
  public FixGenerationResponseDTO generateFix(FixGenerationRequestDTO request) {
    boolean useImage =
        request.screenshot() != null
            && !request.screenshot().isBlank()
            && "image-alt".equals(request.wcagRuleId());

    String prompt = buildPrompt(request, useImage);

    try {
      var builder = MessageCreateParams.builder().model(model).maxTokens(MAX_TOKENS);

      if (useImage) {
        builder.addUserMessageOfBlockParams(
            List.of(
                ContentBlockParam.ofText(TextBlockParam.builder().text(prompt).build()),
                ContentBlockParam.ofImage(
                    ImageBlockParam.builder()
                        .source(
                            Base64ImageSource.builder()
                                .data(stripDataUrl(request.screenshot()))
                                .mediaType(Base64ImageSource.MediaType.IMAGE_PNG)
                                .build())
                        .build())));
      } else {
        builder.addUserMessage(prompt);
      }

      Message message = client.messages().create(builder.build());

      String responseText = extractResponseText(message);
      Optional<String> generatedHtml = FixTagExtractor.extract(responseText);

      if (generatedHtml.isEmpty()) {
        return failure("No <fix> tag found in response. Raw response: " + responseText);
      }

      return new FixGenerationResponseDTO(generatedHtml.get(), model, promptVersion, true, null);

    } catch (AnthropicException e) {
      return failure("Anthropic API error: " + e.getMessage());
    }
  }

  @Override
  public String detectLanguage(String text) {
    try {
      var params =
          MessageCreateParams.builder()
              .model(model)
              .maxTokens(10L)
              .addUserMessage(
                  "Return ONLY the ISO 639-1 two-letter language code "
                      + "of the following text, nothing else:\n\n"
                      + text)
              .build();

      Message message = client.messages().create(params);
      String code = extractResponseText(message).trim().toLowerCase();

      return code.length() == 2 ? code : null;
    } catch (AnthropicException e) {
      return null;
    }
  }

  @Override
  public ProviderType getProviderType() {
    return ProviderType.ANTHROPIC;
  }

  private String stripDataUrl(String screenshot) {
    int index = screenshot.indexOf("base64,");
    return index >= 0 ? screenshot.substring(index + 7) : screenshot;
  }

  private String buildPrompt(FixGenerationRequestDTO request, boolean useImage) {
      boolean useRuleSpecific = useImage || "color-contrast".equals(request.wcagRuleId());
      String template =
          useRuleSpecific
            ? promptLoader.loadForRule(PROMPT_NAME, request.wcagRuleId(), promptVersion)
            : promptLoader.load(PROMPT_NAME, promptVersion);
      Map<String, String> variables = new LinkedHashMap<>();
      variables.put("wcagRuleId", request.wcagRuleId());
      variables.put("impact", request.impact() == null ? "" : request.impact().name());
      variables.put("description", request.description());
      variables.put("targetSelector", request.targetSelector());
      variables.put("htmlSnippet", request.htmlSnippet());
      variables.put("fgColor", request.fgColor());
      variables.put("bgColor", request.bgColor());
      variables.put(
          "contrastRatio",
          request.contrastRatio() == null ? "" : request.contrastRatio().toString());
      variables.put("expectedContrastRatio", request.expectedContrastRatio());
      return promptLoader.fill(template, variables);
    String template =
        useImage
            ? promptLoader.loadForRule(PROMPT_NAME, request.wcagRuleId(), promptVersion)
            : promptLoader.load(PROMPT_NAME, promptVersion);
    Map<String, String> variables = new LinkedHashMap<>();
    variables.put("wcagRuleId", request.wcagRuleId());
    variables.put("impact", request.impact() == null ? "" : request.impact().name());
    variables.put("description", request.description());
    variables.put("targetSelector", request.targetSelector());
    variables.put("htmlSnippet", request.htmlSnippet());
    variables.put(
        "language",
        request.language() == null || request.language().isBlank()
            ? "the same language as the page content"
            : "the language with ISO 639-1 code \"" + request.language() + "\"");
    return promptLoader.fill(template, variables);
  }

  private String extractResponseText(Message message) {
    return message.content().stream()
        .flatMap(block -> block.text().stream())
        .map(textBlock -> textBlock.text())
        .reduce("", (a, b) -> a + b);
  }

  private FixGenerationResponseDTO failure(String reason) {
    return new FixGenerationResponseDTO(null, model, promptVersion, false, reason);
  }
}
