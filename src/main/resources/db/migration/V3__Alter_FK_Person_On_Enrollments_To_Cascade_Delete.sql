ALTER TABLE enrollments
DROP CONSTRAINT fk_enrollments_on_participant;

ALTER TABLE enrollments
    ADD CONSTRAINT fk_enrollments_on_participant
        FOREIGN KEY (participant_id)
            REFERENCES person(id)
            ON DELETE CASCADE;

