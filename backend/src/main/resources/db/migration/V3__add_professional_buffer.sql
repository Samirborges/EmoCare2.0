ALTER TABLE professionals
    ADD COLUMN buffer_minutes SMALLINT NOT NULL DEFAULT 10
        CHECK (buffer_minutes >= 0 AND buffer_minutes <= 30);