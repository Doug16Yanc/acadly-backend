ALTER TABLE events
    ADD final_date_time TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE events
    ADD initial_date_time TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE events
    ADD workload INTEGER;

DROP TABLE classification CASCADE;

ALTER TABLE events
DROP
COLUMN final_date;

ALTER TABLE events
DROP
COLUMN initial_date;

DROP SEQUENCE classification_seq CASCADE;

ALTER TABLE activities
DROP
COLUMN date_time;

ALTER TABLE activities
    ADD date_time TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE certificates
DROP
COLUMN emission_date;

ALTER TABLE certificates
    ADD emission_date TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE events
    ALTER COLUMN is_active DROP NOT NULL;