ALTER TABLE certificates
    ADD email_sent BOOLEAN;

ALTER TABLE certificates
    ALTER COLUMN email_sent SET NOT NULL;