SELECT
  project_id,
  responsible_id
FROM
  kanban.project_responsible;

INSERT INTO
  kanban.project_responsible (project_id, responsible_id)
VALUES
  -- Project 1 (Refatoração Auth) -> Responsáveis 1 (Admin), 2 (Alice), 3 (Bruno)
  -- Project 2 (Log Auditoria) -> Responsáveis 3 (Bruno), 7 (Felipe)
  -- Project 3 (API Usuários - DONE) -> Responsável 4 (Carla)
  -- Project 4 (Docker-Compose - DONE) -> Responsável 7 (Felipe)
  -- Project 5 (Migração MongoDB - CANCELLED) -> Responsável 5 (Daniel)
  -- Project 6 (Otimização Queries - DELAYED) -> Responsáveis 3 (Bruno), 6 (Elena)
  -- Project 7 (CI/CD - DELAYED) -> Responsáveis 7 (Felipe), 9 (Hugo)
  -- Project 8 (Design System) -> Responsáveis 2 (Alice), 4 (Carla), 8 (Giovanna)
  -- Project 9 (Teste de Carga) -> Responsáveis 5 (Daniel), 10 (Isabela)
  -- Project 10 (Documentação - DONE) -> Responsável 1 (Admin)
ON CONFLICT DO NOTHING;