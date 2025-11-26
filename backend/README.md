# Como rodar com Docker (local)

1. Build e up:
```bash
docker-compose up --build
```

2. O fluxo:
- O serviço `db` (Postgres) sobe e executa a migration inicial (via ./db/migrations montada em /docker-entrypoint-initdb.d).
- O backend aguarda o DB estar `healthy` (depends_on com condition service_healthy).
- Ao iniciar, o Spring Boot com Flyway também pode aplicar migrations caso estejam no classpath (recomendado para CI).

3. Acesso:
- API: http://localhost:8080
- DB: localhost:5432 (usuário: kanban / senha: kanban)
