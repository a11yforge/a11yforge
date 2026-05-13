package at.a11yforge.api.review;


import at.a11yforge.api.fixproposal.FixProposal;
import at.a11yforge.api.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fix_proposal_id", nullable = false)
    private FixProposal fixProposal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false, length = 20)
    private ReviewDecision decision;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "decided_at", nullable = false)
    private Instant decidedAt;

    protected Review() {}

    public Review(FixProposal fixProposal, User user, ReviewDecision decision) {
        this.fixProposal = fixProposal;
        this.user = user;
        this.decision = decision;
        this.decidedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public FixProposal getFixProposal() {
        return fixProposal;
    }

    public User getUser() {
        return user;
    }

    public ReviewDecision getDecision() {
        return decision;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getDecidedAt() {
        return decidedAt;
    }


}
