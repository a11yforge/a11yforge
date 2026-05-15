package at.a11yforge.api.page;



import jakarta.persistence.*;
import at.a11yforge.api.scan.Scan;

import java.time.Instant;

@Entity
@Table(name = "page")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scan_id", nullable = false)
    private Scan scan;

    @Column(name = "url", nullable = false, length = 2048)
    private String url;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "scan_status", nullable = false, length = 20)
    private PageStatus scanStatus;

    @Column(name ="rendered_html", columnDefinition = "TEXT")
    private String renderedHtml;

    @Column(name = "fetched_at", nullable = false)
    private Instant fetchedAt;

    protected Page() {}

    public Page(Scan scan, String url) {
        this.scan = scan;
        this.url = url;
        this.scanStatus = PageStatus.SCANNED;
        this.fetchedAt = Instant.now();
    }


    public Long getId() {
        return id;
    }

    public Scan getScan() {
        return scan;
    }


    public String getUrl() {
        return url;
    }


    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public PageStatus getScanStatus() {
        return scanStatus;
    }
    public void setScanStatus(PageStatus scanStatus) {
        this.scanStatus = scanStatus;
    }

    public String getRenderedHtml() {
        return renderedHtml;
    }
    public void setRenderedHtml(String renderedHtml) {
        this.renderedHtml = renderedHtml;
    }

    public Instant getFetchedAt() {
        return fetchedAt;
    }


   }
