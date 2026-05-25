package at.a11yforge.api.page;

import at.a11yforge.api.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pages")
public class PageController {

    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    // HTTP 200 OK
    @GetMapping
    public List<PageResponseDTO> getPages(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam Long scanId) {
        return pageService.getPagesForUser(principal.getId(), scanId);
    }

    // HTTP 200 OK
    @GetMapping("/{pageId}")
    public PageResponseDTO getPage(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long pageId) {
        return pageService.getPageByIdForUser(principal.getId(), pageId);
    }
}
