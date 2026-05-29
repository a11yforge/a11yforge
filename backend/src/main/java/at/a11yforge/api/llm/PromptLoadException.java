package at.a11yforge.api.llm;

public class PromptLoadException extends RuntimeException {
    public PromptLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
