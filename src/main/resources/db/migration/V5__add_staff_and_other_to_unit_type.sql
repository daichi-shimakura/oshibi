ALTER TABLE comedian_profiles
DROP CONSTRAINT chk_unit_type;

ALTER TABLE comedian_profiles
    ADD CONSTRAINT chk_unit_type
        CHECK (unit_type IN ('PIN', 'COMBI', 'TRIO', 'GROUP', 'STAFF', 'OTHER'));