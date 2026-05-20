package at.a11yforge.api.project;

import at.a11yforge.api.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // HTTP 201 Created
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponseDTO createProject(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody ProjectRequestDTO request) {
        return projectService.createProject(principal.getId(), request);
    }

    // HTTP 200 OK
    @GetMapping
    public List<ProjectResponseDTO> getProjects(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return projectService.getProjectsForUser(principal.getId());
    }

    // HTTP 200 OK
    @GetMapping("/{projectId}")
    public ProjectResponseDTO getProject(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long projectId) {
        return projectService.getProjectByIdForUser(principal.getId(), projectId);
    }

    // HTTP 200 OK
    @PutMapping("/{projectId}")
    public ProjectResponseDTO updateProject(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequestDTO request) {
        return projectService.updateProject(principal.getId(), projectId, request);
    }

    // HTTP 204 No Content
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long projectId) {
        projectService.deleteProject(principal.getId(), projectId);
    }
}
