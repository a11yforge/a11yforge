package at.a11yforge.api.page;

import org.springframework.stereotype.Service;

@Service
public class PageService {

    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }
}
