package at.a11yforge.api.llm;

public interface ChatProvider {

    FixGenerationResponseDTO generateFix(FixGenerationRequestDTO request);

    ProviderType getProviderType();
}
