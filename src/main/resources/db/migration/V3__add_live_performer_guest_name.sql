ALTER TABLE live_performers ALTER COLUMN account_id DROP NOT NULL;

ALTER TABLE live_performers ADD COLUMN guest_name VARCHAR(100);

ALTER TABLE live_performers
    ADD CONSTRAINT chk_account_id_or_guest_name
        CHECK (account_id IS NOT NULL OR guest_name IS NOT NULL);