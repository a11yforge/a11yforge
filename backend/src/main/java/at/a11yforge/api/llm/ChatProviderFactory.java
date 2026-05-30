package at.a11yforge.api.llm;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ChatProviderFactory {

    private final Map<ProviderType, ChatProvider> providersByType;

    public ChatProviderFactory(List<ChatProvider> providers) {
        this.providersByType = providers.stream()
                .collect(Collectors.toMap(
                        ChatProvider::getProviderType,
                        Function.identity()
                ));
    }

    public ChatProvider getProvider(ProviderType type) {
        ChatProvider provider = providersByType.get(type);
        if (provider == null) {
            throw new IllegalStateException(
                    "No ChatProvider registered for type: " + type
            );
        }
        return provider;
    }
}
