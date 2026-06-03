package at.a11yforge.api.project;

import at.a11yforge.api.user.User;
import at.a11yforge.api.user.UserNotFoundException;
import at.a11yforge.api.user.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import at.a11yforge.api.auditevent.AuditEventService;
import at.a11yforge.api.auditevent.AuditEventType;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AuditEventService auditEventService;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository,
                          AuditEventService auditEventService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.auditEventService = auditEventService;
    }

    @Transactional
    public ProjectResponseDTO createProject(Long userId, ProjectRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Project project = new Project(user, request.name(), request.baseUrl());
        if (request.crawlMaxPages() != null) {
            project.setCrawlMaxPages(request.crawlMaxPages());
        }

        try {
            Project saved = projectRepository.save(project);
            return toResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateProjectNameException(request.name());
        }
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getProjectsForUser(Long userId) {
        return projectRepository.findAllByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectByIdForUser(Long userId, Long projectId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        return toResponse(project);
    }

    @Transactional
    public ProjectResponseDTO updateProject(Long userId, Long projectId, ProjectRequestDTO request) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        project.setName(request.name());
        project.setBaseUrl(request.baseUrl());
        if (request.crawlMaxPages() != null) {
            project.setCrawlMaxPages(request.crawlMaxPages());
        }

        try {
            Project saved = projectRepository.save(project);
            return toResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateProjectNameException(request.name());
        }
    }

    @Transactional
    public void deleteProject(Long userId, Long projectId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        auditEventService.recordEvent(
                userId, "Project", project.getId(),
                AuditEventType.DELETED, project.getName(), null);

        projectRepository.delete(project);
    }

    private ProjectResponseDTO toResponse(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getBaseUrl(),
                project.getCrawlMaxPages(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
