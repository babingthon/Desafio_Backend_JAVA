CREATE SCHEMA IF NOT EXISTS kanban;

CREATE TABLE IF NOT EXISTS kanban.users (
                                            id BIGSERIAL PRIMARY KEY,
                                            email VARCHAR(200) NOT NULL UNIQUE,
                                            password VARCHAR(200) NOT NULL,
                                            role VARCHAR(50) NOT NULL,
                                            created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
                                            updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS kanban.responsible (
                                                  id BIGSERIAL PRIMARY KEY,
                                                  name VARCHAR(200) NOT NULL,
                                                  email VARCHAR(200) NOT NULL UNIQUE,
                                                  job_title VARCHAR(100),
                                                  created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
                                                  updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS kanban.project (
                                              id BIGSERIAL PRIMARY KEY,
                                              name VARCHAR(255) NOT NULL,
                                              description TEXT,
                                              status VARCHAR(50) NOT NULL,
                                              scheduled_start_date DATE NOT NULL,
                                              scheduled_end_date DATE NOT NULL,
                                              responsible_id BIGINT REFERENCES kanban.responsible(id) ON DELETE RESTRICT,
                                              created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
                                              updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);