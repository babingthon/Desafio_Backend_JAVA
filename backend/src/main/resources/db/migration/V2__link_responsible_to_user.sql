ALTER TABLE kanban.responsible
    ADD COLUMN user_id BIGINT;

ALTER TABLE kanban.responsible
    ADD CONSTRAINT fk_responsible_user
        FOREIGN KEY (user_id)
            REFERENCES kanban.users (id);

ALTER TABLE kanban.responsible
    ADD CONSTRAINT uk_responsible_user_id UNIQUE (user_id);