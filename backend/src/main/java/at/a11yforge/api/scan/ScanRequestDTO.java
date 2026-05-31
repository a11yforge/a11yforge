package at.a11yforge.api.scan;

import at.a11yforge.api.llm.ProviderType;

public record ScanRequestDTO(Long projectId, ProviderType llmProvider) {}
