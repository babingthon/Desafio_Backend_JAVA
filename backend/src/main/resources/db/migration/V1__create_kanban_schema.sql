CREATE SCHEMA IF NOT EXISTS kanban;

CREATE TABLE IF NOT EXISTS kanban.user_account (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS kanban.board (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS kanban.status_column (
    id BIGSERIAL PRIMARY KEY,
    board_id BIGINT NOT NULL REFERENCES kanban.board(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    position_index INTEGER NOT NULL,
    wip_limit INTEGER,
    is_default_done BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    UNIQUE (board_id, position_index),
    UNIQUE (board_id, name)
);

CREATE TABLE IF NOT EXISTS kanban.card (
    id BIGSERIAL PRIMARY KEY,
    board_id BIGINT NOT NULL REFERENCES kanban.board(id) ON DELETE CASCADE,
    status_column_id BIGINT NOT NULL REFERENCES kanban.status_column(id) ON DELETE RESTRICT,
    user_id BIGINT REFERENCES kanban.user_account(id),
    title VARCHAR(250) NOT NULL,
    description TEXT,
    priority SMALLINT DEFAULT 3,
    estimate_hours INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    started_at TIMESTAMP WITHOUT TIME ZONE,
    finished_at TIMESTAMP WITHOUT TIME ZONE,
    due_date DATE,
    external_reference VARCHAR(200)
);

CREATE INDEX IF NOT EXISTS idx_card_status ON kanban.card(status_column_id);
CREATE INDEX IF NOT EXISTS idx_card_user ON kanban.card(user_id);
CREATE INDEX IF NOT EXISTS idx_status_board_position ON kanban.status_column(board_id, position_index);

CREATE OR REPLACE FUNCTION kanban.set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_board_updated_at ON kanban.board;
CREATE TRIGGER trg_board_updated_at 
BEFORE UPDATE ON kanban.board
FOR EACH ROW EXECUTE FUNCTION kanban.set_updated_at();

DROP TRIGGER IF EXISTS trg_status_column_updated_at ON kanban.status_column;
CREATE TRIGGER trg_status_column_updated_at 
BEFORE UPDATE ON kanban.status_column
FOR EACH ROW EXECUTE FUNCTION kanban.set_updated_at();

DROP TRIGGER IF EXISTS trg_card_updated_at ON kanban.card;
CREATE TRIGGER trg_card_updated_at 
BEFORE UPDATE ON kanban.card
FOR EACH ROW EXECUTE FUNCTION kanban.set_updated_at();

INSERT INTO kanban.user_account (name, email, password, role)
SELECT 'Administrator', 'admin@kanban.com', 'admin', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM kanban.user_account WHERE email = 'admin@kanban.com');

INSERT INTO kanban.board (name, description) 
SELECT 'Main Board', 'Default board'
WHERE NOT EXISTS (SELECT 1 FROM kanban.board WHERE name = 'Main Board');

WITH b AS (SELECT id FROM kanban.board WHERE name = 'Main Board' LIMIT 1)
INSERT INTO kanban.status_column (board_id, name, position_index, wip_limit, is_default_done)
SELECT b.id, v.name, v.pos, NULL, v.is_done
FROM b 
CROSS JOIN (VALUES
  ('Backlog', 1, false),
  ('To Do', 2, false),
  ('Doing', 3, false),
  ('Review', 4, false),
  ('Done', 5, true)
) AS v(name, pos, is_done)
WHERE NOT EXISTS (
  SELECT 1 
  FROM kanban.status_column sc 
  WHERE sc.board_id = b.id AND sc.name = v.name
);
