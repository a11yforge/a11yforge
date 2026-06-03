ALTER TABLE project DROP CONSTRAINT fk_project_user_id;
ALTER TABLE project ADD CONSTRAINT fk_project_user_id
    FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE;

ALTER TABLE scan DROP CONSTRAINT fk_scan_project_id;
ALTER TABLE scan ADD CONSTRAINT fk_scan_project_id
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE;

ALTER TABLE page DROP CONSTRAINT fk_page_scan_id;
ALTER TABLE page ADD CONSTRAINT fk_page_scan_id
    FOREIGN KEY (scan_id) REFERENCES scan (id) ON DELETE CASCADE;

ALTER TABLE violation DROP CONSTRAINT fk_violation_page_id;
ALTER TABLE violation ADD CONSTRAINT fk_violation_page_id
    FOREIGN KEY (page_id) REFERENCES page (id) ON DELETE CASCADE;

ALTER TABLE fix_proposal DROP CONSTRAINT fk_fix_proposal_violation_id;
ALTER TABLE fix_proposal ADD CONSTRAINT fk_fix_proposal_violation_id
    FOREIGN KEY (violation_id) REFERENCES violation (id) ON DELETE CASCADE;

ALTER TABLE review DROP CONSTRAINT fk_review_fix_proposal_id;
ALTER TABLE review ADD CONSTRAINT fk_review_fix_proposal_id
    FOREIGN KEY (fix_proposal_id) REFERENCES fix_proposal (id) ON DELETE CASCADE;

ALTER TABLE review DROP CONSTRAINT fk_review_user_id;
ALTER TABLE review ADD CONSTRAINT fk_review_user_id
    FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE;

ALTER TABLE audit_event DROP CONSTRAINT fk_audit_event_user_id;
ALTER TABLE audit_event ADD CONSTRAINT fk_audit_event_user_id
    FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE SET NULL;