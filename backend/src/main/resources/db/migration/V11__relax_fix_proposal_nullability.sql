ALTER TABLE fix_proposal
    ALTER COLUMN generated_html DROP NOT NULL,
    ALTER COLUMN llm_model DROP NOT NULL,
    ALTER COLUMN prompt_version DROP NOT NULL;