package at.a11yforge.api.page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PageService {

    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Transactional(readOnly = true)
    public List<PageResponseDTO> getPagesForUser(Long userId, Long scanId) {
        return pageRepository.findAllByScanIdAndScanProjectUserId(scanId, userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponseDTO getPageByIdForUser(Long userId, Long pageId) {
        Page page = pageRepository.findByIdAndScanProjectUserId(pageId, userId)
                .orElseThrow(() -> new PageNotFoundException(pageId));
        return toResponse(page);
    }

    private PageResponseDTO toResponse(Page page) {
        return new PageResponseDTO(
                page.getId(),
                page.getScan().getId(),
                page.getUrl(),
                page.getHttpStatus(),
                page.getScanStatus(),
                page.getFetchedAt()
        );
    }
}
