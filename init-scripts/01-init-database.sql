-- Script de inicialização do banco de dados
-- Este script é executado automaticamente quando o container PostgreSQL é criado

-- Criar extensões se necessário
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Comentário sobre o banco
COMMENT ON DATABASE project_manager IS 'Banco de dados para o sistema de gerenciamento de projetos e funcionários';

-- As tabelas serão criadas automaticamente pelo Hibernate
-- com spring.jpa.hibernate.ddl-auto=update
