INSERT INTO kanban."users" (id, name, email, password, role, created_at, updated_at)
VALUES (
  1,
  'admin',
  'admin@example.com',
  '$2a$10$w/XJ9qZ3w8Q6Y4H0k.E2p.K7vFfGvV0h.L7oO9cT1tP2uA3sD4s',
  'ADMIN',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;