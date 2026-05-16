package at.a11yforge.api.project;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ProjectRequestDTO request) {
        return projectService.createProject(userId, request);
    }

    // HTTP 200 OK
    @GetMapping
    public List<ProjectResponseDTO> getProjects(
            @RequestHeader("X-User-Id") Long userId) {
        return projectService.getProjectsForUser(userId);
    }

    // HTTP 200 OK
    @GetMapping("/{projectId}")
    public ProjectResponseDTO getProject(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long projectId) {
        return projectService.getProjectByIdForUser(userId, projectId);
    }

    // HTTP 200 OK
    @PutMapping("/{projectId}")
    public ProjectResponseDTO updateProject(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequestDTO request) {
        return projectService.updateProject(userId, projectId, request);
    }

    // HTTP 204 No Content
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long projectId) {
        projectService.deleteProject(userId, projectId);
    }
}

