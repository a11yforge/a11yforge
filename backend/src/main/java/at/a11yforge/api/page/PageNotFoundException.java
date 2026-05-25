package at.a11yforge.api.page;

public class PageNotFoundException extends RuntimeException {

    public PageNotFoundException(Long pageId) {
        super("Page with id " + pageId + " not found");
    }
}
