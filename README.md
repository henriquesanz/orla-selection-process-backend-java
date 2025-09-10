# Processo Seletivo Orla - CRUD Java com PostgreSQL

## Visão Geral

Para apoio visual do funcionamento, foi criado um frontend simples com HTML e Javascript para auxiliar no teste do funcionamento do app principal, sendo o Backend em Java, como base de dados relacional optei por utilizar o PostgreSQL e framework do Java estou utilizando o SpringBoot.

1. **Frontend** (Nginx) - Porta 3000
2. **Backend** (Spring Boot) - Porta 8080  
3. **Database** (PostgreSQL) - Porta 5432

## Como Usar

### 1. Inicialização Rápida

**Windows:**
```bash
docker-compose up --build -d
```

### 2. Acessos

Após inicialização:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health

### 3. Testes com Postman

Para testar a API, você pode usar a collection do Postman disponível no arquivo `Backend-Java-MVP-Project-Manager.json`.

**Como importar a collection:**
1. Abra o Postman
2. Clique em "Import" no canto superior esquerdo
3. Selecione o arquivo `Backend-Java-MVP-Project-Manager.json` do projeto
4. A collection será importada com todos os endpoints configurados

**Endpoints disponíveis na collection:**
- **Funcionários**: CRUD simples (criar, listar, buscar por ID)
- **Projetos**: CRUD simples (criar, listar, buscar por ID)

**Configuração:**
- Base URL: `http://localhost:8080`
- A collection já está configurada para usar a URL base correta
- Todos os endpoints incluem exemplos de requisição e resposta

### 4. Comandos Úteis

```bash
# Ver status
docker-compose ps

# Ver logs
docker-compose logs -f

# Parar tudo
docker-compose down

## Arquitetura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   PostgreSQL    │
│   (Nginx)       │    │   (Spring Boot) │    │   (Database)    │
│   Port: 3000    │◄──►│   Port: 8080    │◄──►│   Port: 5432    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## Fluxo de Dados

1. **Frontend** (index.html) é servido pelo Nginx na porta 3000
2. **Frontend** faz requisições AJAX para `/api/*`
3. **Nginx** faz proxy das requisições `/api/*` para o **Backend** (porta 8080)
4. **Backend** processa as requisições e acessa o **PostgreSQL** (porta 5432)
5. **Dados** são persistidos no volume `postgres_data`

## Configurações de Rede

- Todos os serviços estão na rede `project-network`
- Comunicação interna usa nomes dos containers
- Frontend acessa Backend via `http://backend:8080`
- Backend acessa DB via `jdbc:postgresql://postgres:5432/project_manager`

## Health Checks

- **PostgreSQL**: `pg_isready -U postgres -d project_manager`
- **Backend**: `curl http://localhost:8080/actuator/health`
- **Frontend**: `curl http://localhost:3000/health`

## Volumes Persistentes

- `postgres_data`: Dados do PostgreSQL persistem entre reinicializações
- `index.html`: Montado como volume para facilitar desenvolvimento

## Desenvolvimento

### Ver Logs
```bash
# Todos os serviços
docker-compose logs -f

# Serviço específico
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres
```

## Troubleshooting

### Porta já em uso
```bash
# Verificar portas
netstat -an | findstr :3000
netstat -an | findstr :8080
netstat -an | findstr :5432
```

### Container não inicia
```bash
# Ver logs
docker-compose logs <servico>

# Rebuild
docker-compose build --no-cache
```

### Frontend não carrega
```bash
# Verificar se index.html existe
ls -la index.html

# Testar backend diretamente
curl http://localhost:8080/api/funcionarios
```

## Limpeza

```bash
# Parar e remover containers
docker-compose down

# Remover volumes (CUIDADO: apaga dados)
docker-compose down -v

# Limpeza completa
docker system prune -a
```

Obs.: Alguns arquivos auxiliares e documentação do projeto foram criados com auxilio de agentes para acelerar o processo.