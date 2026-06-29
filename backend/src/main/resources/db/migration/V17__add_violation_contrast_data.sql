ALTER TABLE violation ADD COLUMN fg_color VARCHAR(30);
ALTER TABLE violation ADD COLUMN bg_color VARCHAR(30);
ALTER TABLE violation ADD COLUMN contrast_ratio NUMERIC(5,2);
ALTER TABLE violation ADD COLUMN expected_contrast_ratio VARCHAR(15);
