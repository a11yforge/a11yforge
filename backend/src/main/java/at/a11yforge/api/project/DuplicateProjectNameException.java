package at.a11yforge.api.project;

public class DuplicateProjectNameException extends RuntimeException {
    public DuplicateProjectNameException(String name) {
        super("Project name already exists for this user: " + name);
    }
}
