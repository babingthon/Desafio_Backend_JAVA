# ----------------------------------------------------------------------------------
# STAGE 1: BUILD (Compilação) - CORREÇÃO: FROM deve vir primeiro
# ----------------------------------------------------------------------------------
# Esta linha DEVE ser a primeira instrução
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# 1. Copia o pom.xml
COPY pom.xml .

# 2. Baixa dependências
RUN mvn dependency:go-offline

# 3. Copia o código fonte
COPY src src

# 4. Faz o build completo
RUN mvn clean package -DskipTests

# ----------------------------------------------------------------------------------
# STAGE 2: RUNTIME (Execução)
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-jammy

# Cria um usuário não-root (Segurança)
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

ARG JAR_FILE=target/*.jar

COPY --from=builder /app/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]