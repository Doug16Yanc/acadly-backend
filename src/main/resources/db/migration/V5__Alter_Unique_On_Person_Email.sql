ALTER TABLE person
    ADD CONSTRAINT uc_person_email UNIQUE (email);