package at.a11yforge.api.project;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long projectId) {
        super("Project not found: " + projectId);
    }
}
