SELECT
  id,
  name,
  description,
  status,
  scheduled_start_date,
  scheduled_end_date,
  responsible_id,
  created_at,
  updated_at,
  actual_end_date,
  actual_start_date
FROM
  kanban.project;

DO $VAR$
DECLARE
    today DATE := NOW()::DATE;
BEGIN

    INSERT INTO kanban.project (
        id, name, description, status, 
        scheduled_start_date, scheduled_end_date, 
        responsible_id, created_at, updated_at, 
        actual_start_date, actual_end_date
    ) VALUES
    (1, 'Criação da API de Usuários', 'Desenvolvimento completo dos endpoints de users/responsible.', 'DONE', 
        today - INTERVAL '4 months', today - INTERVAL '3 months', 
        4, NOW(), NOW(), 
        today - INTERVAL '4 months', today - INTERVAL '3 months'),
    (2, 'Configuração do Docker-Compose', 'Ambiente de desenvolvimento completo com Docker.', 'DONE', 
        today - INTERVAL '2 months', today - INTERVAL '1 month', 
        1, NOW(), NOW(), 
        today - INTERVAL '2 months', today - INTERVAL '1 month'),
    (3, 'Refatoração da Autenticação JWT', 'Mudar a estrutura de tokens e refresh tokens.', 'IN_PROGRESS', 
        today - INTERVAL '1 week', today + INTERVAL '1 month', 
        3, NOW(), NOW(), 
        today - INTERVAL '1 week', NULL),
    (4, 'Implementação do Log de Auditoria', 'Adicionar registros de auditoria em todas as operações CRUD críticas.', 'IN_PROGRESS', 
        today - INTERVAL '3 days', today + INTERVAL '2 weeks', 
        2, NOW(), NOW(), 
        today - INTERVAL '3 days', NULL),
    (5, 'Otimização de Consultas Lentas', 'Revisar queries com tempo de resposta acima de 500ms.', 'IN_PROGRESS', 
        today - INTERVAL '2 months', today - INTERVAL '1 week', 
        7, NOW(), NOW(), 
        today - INTERVAL '2 months', NULL),
    (6, 'Implementação do CI/CD no Gitlab', 'Automatizar deploy após merge na branch main.', 'IN_PROGRESS', 
        today - INTERVAL '1 month', today - INTERVAL '2 days', 
        6, NOW(), NOW(), 
        today - INTERVAL '1 month', NULL),
    (7, 'Design System V2', 'Atualização completa do design e componentes de frontend.', 'ON_HOLD', 
        today + INTERVAL '1 month', today + INTERVAL '6 months', 
        8, NOW(), NOW(), 
        NULL, NULL), 
    (8, 'Migração para MongoDB', 'Projeto de POC cancelado devido a requisitos de transação.', 'CANCELLED', 
        today - INTERVAL '8 months', today - INTERVAL '6 months', 
        5, NOW(), NOW(), 
    (9, 'Teste de Carga com JMeter', 'Simulação de 500 usuários concorrentes.', 'TO_DO', 
        today + INTERVAL '1 month', today + INTERVAL '2 months', 
        9, NOW(), NOW(), 
        NULL, NULL),
    (10, 'Documentação OpenAPI (Swagger)', 'Documentação completa de todos os endpoints.', 'TO_DO', 
        today + INTERVAL '1 week', today + INTERVAL '2 weeks', 
        10, NOW(), NOW(), 
        NULL, NULL)
    ON CONFLICT (id) DO NOTHING;

END $VAR$;