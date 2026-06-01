package at.a11yforge.api.llm;

public record OllamaRequestDTO(String model, String prompt, boolean stream) {}
