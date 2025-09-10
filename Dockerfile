# Multi-stage build para otimizar o tamanho da imagem
FROM maven:latest AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .

# Baixar dependências (cache layer)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src src

# Compilar a aplicação
RUN mvn clean package -DskipTests

# Imagem final
FROM eclipse-temurin:21-jre

# Instalar curl para healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Criar usuário não-root
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Definir diretório de trabalho
WORKDIR /app

# Copiar JAR da aplicação
COPY --from=build /app/target/*.jar app.jar

# Copiar arquivos estáticos (frontend)
COPY index.html /app/static/

# Alterar proprietário dos arquivos
RUN chown -R spring:spring /app

# Mudar para usuário não-root
USER spring:spring

# Expor porta
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
