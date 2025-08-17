ALTER TABLE certificates
DROP CONSTRAINT fk_certificates_on_enrollment;

ALTER TABLE certificates
    ADD CONSTRAINT fk_certificates_on_enrollment
        FOREIGN KEY (enrollment_id)
            REFERENCES enrollments(id)
            ON DELETE CASCADE;