SELECT
  id,
  name,
  email,
  job_title,
  created_at,
  updated_at,
  user_id
FROM
  kanban.responsible;

INSERT INTO
  kanban.responsible (
    id,
    name,
    email,
    job_title,
    user_id,
    created_at,
    updated_at
  )
VALUES
  (
    1,
    'Administrador Master',
    'admin.master@kanban.com',
    'CTO & Admin',
    1,
    NOW(),
    NOW()
  ),
  (
    2,
    'Alice Silva',
    'alice.silva@kanban.com',
    'UX Designer',
    2,
    NOW(),
    NOW()
  ),
  (
    3,
    'Bruno Mendes',
    'bruno.mendes@kanban.com',
    'Backend Developer',
    3,
    NOW(),
    NOW()
  ),
  (
    4,
    'Carla Dias',
    'carla.dias@kanban.com',
    'Frontend Developer',
    4,
    NOW(),
    NOW()
  ),
  (
    5,
    'Daniel Rocha',
    'daniel.rocha@kanban.com',
    'QA Tester',
    5,
    NOW(),
    NOW()
  ),
  (
    6,
    'Elena Pires',
    'elena.pires@kanban.com',
    'Product Manager',
    6,
    NOW(),
    NOW()
  ),
  (
    7,
    'Felipe Souza',
    'felipe.souza@kanban.com',
    'DevOps Engineer',
    7,
    NOW(),
    NOW()
  ),
  (
    8,
    'Giovanna Neves',
    'giovanna.neves@kanban.com',
    'Data Analyst',
    8,
    NOW(),
    NOW()
  ),
  (
    9,
    'Hugo Lima',
    'hugo.lima@kanban.com',
    'Mobile Developer',
    9,
    NOW(),
    NOW()
  ),
  (
    10,
    'Isabela Costa',
    'isabela.costa@kanban.com',
    'Scrum Master',
    10,
    NOW(),
    NOW()
  )
ON CONFLICT (id) DO NOTHING;