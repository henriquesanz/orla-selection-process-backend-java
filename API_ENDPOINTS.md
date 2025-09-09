# API Endpoints - Project Manager

## Funcionários

### Criar Funcionário
**POST** `/api/funcionarios`

**Request Body (sem projetos):**
```json
{
  "nome": "João Silva",
  "cpf": "12345678901",
  "email": "joao.silva@email.com",
  "salario": 5000.00,
  "projetos": null
}
```

**Request Body (com projetos existentes):**
```json
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria.santos@email.com",
  "salario": 6000.00,
  "projetos": [
    {
      "nome": "Sistema de Gestão",
      "descricao": "Projeto existente"
    }
  ]
}
```

**Request Body (criando novos projetos):**
```json
{
  "nome": "Pedro Costa",
  "cpf": "11122233344",
  "email": "pedro.costa@email.com",
  "salario": 4500.00,
  "projetos": [
    {
      "nome": "Novo Projeto",
      "descricao": "Projeto que será criado automaticamente"
    }
  ]
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678901",
  "email": "joao.silva@email.com",
  "salario": 5000.00,
  "projetos": []
}
```

### Listar Funcionários
**GET** `/api/funcionarios`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "cpf": "12345678901",
    "email": "joao.silva@email.com",
    "salario": 5000.00,
    "projetos": []
  }
]
```

### Buscar Funcionário por ID
**GET** `/api/funcionarios/{id}`

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678901",
  "email": "joao.silva@email.com",
  "salario": 5000.00,
  "projetos": []
}
```

## Projetos

### Criar Projeto
**POST** `/api/projetos`

**Request Body (sem funcionários):**
```json
{
  "nome": "Sistema de Gestão",
  "descricao": "Desenvolvimento de sistema para gestão de projetos",
  "funcionarios": null
}
```

**Request Body (com funcionários existentes):**
```json
{
  "nome": "Projeto Mobile",
  "descricao": "Desenvolvimento de aplicativo mobile",
  "funcionarios": [
    {
      "nome": "João Silva",
      "cpf": "12345678901",
      "email": "joao.silva@email.com",
      "salario": 5000.00
    }
  ]
}
```

**Request Body (criando novos funcionários):**
```json
{
  "nome": "Projeto Web",
  "descricao": "Desenvolvimento de aplicação web",
  "funcionarios": [
    {
      "nome": "Ana Lima",
      "cpf": "55566677788",
      "email": "ana.lima@email.com",
      "salario": 5500.00
    }
  ]
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nome": "Sistema de Gestão",
  "descricao": "Desenvolvimento de sistema para gestão de projetos",
  "dataCriacao": "2024-01-15T10:30:00",
  "funcionarios": []
}
```

### Listar Projetos
**GET** `/api/projetos`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Sistema de Gestão",
    "descricao": "Desenvolvimento de sistema para gestão de projetos",
    "dataCriacao": "2024-01-15T10:30:00",
    "funcionarios": []
  }
]
```

### Buscar Projeto por ID
**GET** `/api/projetos/{id}`

**Response (200 OK):**
```json
{
  "id": 1,
  "nome": "Sistema de Gestão",
  "descricao": "Desenvolvimento de sistema para gestão de projetos",
  "dataCriacao": "2024-01-15T10:30:00",
  "funcionarios": []
}
```

## Associações Automáticas

### Funcionário com Projetos
- Se um funcionário for criado com projetos na lista `projetos`:
  - **Projetos existentes**: Se o projeto já existe (verificado pelo nome), o funcionário será associado ao projeto existente
  - **Projetos novos**: Se o projeto não existe, ele será criado automaticamente e associado ao funcionário
  - **Validação**: Nomes de projetos devem ser únicos

### Projeto com Funcionários
- Se um projeto for criado com funcionários na lista `funcionarios`:
  - **Funcionários existentes**: Se o funcionário já existe (verificado pelo CPF), ele será associado ao projeto
  - **Funcionários novos**: Se o funcionário não existe, ele será criado automaticamente e associado ao projeto
  - **Validação**: CPF e email de funcionários devem ser únicos

### Comportamento das Associações
- As associações são **bidirecionais**: se A está associado a B, então B também está associado a A
- **Transações**: Todas as operações são executadas em uma única transação
- **Rollback**: Se qualquer operação falhar, todas as mudanças são revertidas
- **Relacionamento Many-to-Many**: Um funcionário pode estar em múltiplos projetos e um projeto pode ter múltiplos funcionários

## Validações

### Funcionário
- **nome**: obrigatório, não pode estar vazio
- **cpf**: obrigatório, não pode estar vazio, deve ser único
- **email**: obrigatório, deve ter formato válido, deve ser único
- **salario**: obrigatório, deve ser positivo

### Projeto
- **nome**: obrigatório, não pode estar vazio, deve ser único
- **descricao**: opcional

## Códigos de Status HTTP

- **200 OK**: Operação realizada com sucesso
- **201 Created**: Recurso criado com sucesso
- **400 Bad Request**: Dados inválidos ou violação de regras de negócio
- **404 Not Found**: Recurso não encontrado
