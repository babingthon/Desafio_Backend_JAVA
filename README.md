# **Desafio Técnico – Backend Kanban**

Implementação de uma API REST para gerenciamento de Projetos utilizando modelo Kanban. O sistema aplica regras de negócio específicas para transição de status, cálculo de métricas, auditoria e autenticação. A arquitetura do backend segue boas práticas com separação de camadas, versionamento de banco com Flyway e execução em ambiente Docker.

---

## **📦 Stack Técnica**

### **Backend**
- Java 17  
- Spring Boot 3  
- Spring Web  
- Spring Data JPA  
- Spring Validation  
- Spring Security (JWT)  
- Spring Boot Actuator  
- Flyway Migration  
- PostgreSQL 15  
- Maven  

### **Infraestrutura**
- Docker / Docker Compose  
- Migrações SQL versionadas  
- Healthcheck (DB e API)  
- Perfis de execução (dev / docker)  

### **Frontend (etapa futura)**
- Angular 17  
- Angular Material  
- CDK Drag & Drop  

---

## **📁 Estrutura do Projeto**

desafio-tecnico-backend/  
│  
├── backend/  
│   ├── src/  
│   ├── pom.xml  
│   └── Dockerfile  
│  
├── frontend/  
│  
├── db/  
│   └── migrations/  
│  
├── docker-compose.yml  
├── .env  
└── README.md

