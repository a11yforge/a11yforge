package at.a11yforge.api.llm;

import org.springframework.stereotype.Component;

@Component
public class NoOpChatProvider implements ChatProvider {

    @Override
    public FixGenerationResponseDTO generateFix(FixGenerationRequestDTO request) {
        return new FixGenerationResponseDTO(
                null,
                null,
                null,
                true,
                null
        );
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.NONE;
    }
}
