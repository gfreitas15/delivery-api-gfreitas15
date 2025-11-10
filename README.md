# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21, com API REST completa e documentação Swagger/OpenAPI.

## 🚀 Tecnologias

- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.5.7
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- Swagger/OpenAPI 3 (springdoc-openapi)
- Lombok

## ⚡ Recursos Modernos Utilizados

- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏗️ Arquitetura

```
[App Mobile] ←→ [API REST] ←→ [Services] ←→ [Database]
[Portal Web] ←→     ↑      ←→ [Business Rules]
[Integrações] ←→ [Swagger UI]
                    ↑
              [Documentação]
```

## 🏃‍♂️ Como executar

1. **Pré-requisitos: JDK 21 instalado**
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run` (Linux/Mac) ou `mvnw.cmd spring-boot:run` (Windows)
4. Acesse: http://localhost:8080/health

## 📋 Endpoints Principais

### Health & Info
- `GET /health` - Status da aplicação (inclui versão Java)
- `GET /info` - Informações da aplicação

### Restaurantes (`/api/restaurantes`)
- `POST /api/restaurantes` - Cadastrar restaurante
- `GET /api/restaurantes` - Listar com filtros (categoria, ativo) e paginação
- `GET /api/restaurantes/{id}` - Buscar por ID
- `PUT /api/restaurantes/{id}` - Atualizar restaurante
- `PATCH /api/restaurantes/{id}/status` - Ativar/desativar
- `GET /api/restaurantes/categoria/{categoria}` - Por categoria
- `GET /api/restaurantes/{id}/taxa-entrega/{cep}` - Calcular taxa
- `GET /api/restaurantes/proximos/{cep}` - Restaurantes próximos

### Produtos (`/api/produtos`)
- `POST /api/produtos` - Cadastrar produto
- `GET /api/produtos/{id}` - Buscar por ID
- `PUT /api/produtos/{id}` - Atualizar produto
- `DELETE /api/produtos/{id}` - Remover produto
- `PATCH /api/produtos/{id}/disponibilidade` - Toggle disponibilidade
- `GET /api/restaurantes/{restauranteId}/produtos` - Produtos do restaurante
- `GET /api/produtos/categoria/{categoria}` - Por categoria
- `GET /api/produtos/buscar?nome={nome}` - Busca por nome

### Pedidos (`/api/pedidos`)
- `POST /api/pedidos` - Criar pedido
- `GET /api/pedidos/{id}` - Buscar pedido completo
- `GET /api/pedidos` - Listar com filtros (status, data) e paginação
- `PATCH /api/pedidos/{id}/status` - Atualizar status
- `DELETE /api/pedidos/{id}` - Cancelar pedido
- `GET /api/clientes/{clienteId}/pedidos` - Histórico do cliente
- `GET /api/restaurantes/{restauranteId}/pedidos` - Pedidos do restaurante
- `POST /api/pedidos/calcular` - Calcular total sem salvar

### Relatórios (`/api/relatorios`)
- `GET /api/relatorios/vendas-por-restaurante` - Vendas por restaurante
- `GET /api/relatorios/produtos-mais-vendidos` - Top produtos
- `GET /api/relatorios/clientes-ativos` - Clientes mais ativos
- `GET /api/relatorios/pedidos-por-periodo` - Pedidos por período

## 📚 Documentação Swagger

A documentação completa da API está disponível via Swagger UI:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs

A documentação inclui:
- Descrição de todos os endpoints
- Exemplos de request/response
- Códigos de status HTTP
- Validações e esquemas
- Try it out (teste interativo)

## 🔧 Configuração

- **Porta**: 8080
- **Banco**: H2 em memória
- **Profile**: development
- **DDL**: update (cria/atualiza tabelas automaticamente)
- **Console H2**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:deliverydb`
  - Username: `sa`
  - Password: (vazio)

## 📦 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/deliverytech/delivery_api/
│   │   ├── config/          # Configurações (Swagger, etc)
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/             # DTOs (Request/Response)
│   │   ├── exception/       # Exceções customizadas
│   │   ├── model/           # Entidades JPA
│   │   ├── repository/      # Repositórios JPA
│   │   └── service/         # Serviços com regras de negócio
│   └── resources/
│       └── application.properties
└── test/
    └── java/.../controller/  # Testes de integração
```

## 🎯 Funcionalidades Implementadas

### ✅ Validações Robustas
- Validações nos DTOs com anotações Jakarta Validation
- Validações customizadas (@ValidCEP, @ValidTelefone, @ValidCategoria)
- Validação de tamanho, formato, valores mínimos/máximos
- Mensagens de erro claras e descritivas

### ✅ Tratamento Global de Exceções
- GlobalExceptionHandler com @ControllerAdvice
- Hierarquia de exceções customizadas (BusinessException, EntityNotFoundException, ValidationException, ConflictException)
- Respostas de erro padronizadas seguindo RFC 7807
- Códigos HTTP apropriados (400, 404, 409, 422, 500)

### ✅ Controllers REST Completos
- RestauranteController com todos os endpoints
- ProdutoController com todos os endpoints
- PedidoController com todos os endpoints
- RelatorioController com endpoints de relatórios

### ✅ Documentação Swagger
- Configuração personalizada com metadados
- Anotações em todos os endpoints
- DTOs documentados com exemplos
- Try it out funcionando

### ✅ Padronização de Respostas
- ApiResponse<T> para respostas padronizadas
- PagedResponse<T> para listagens paginadas
- ErrorResponse para erros padronizados
- Códigos HTTP corretos (200, 201, 204, 400, 404, 409, 500)

### ✅ Validações e Tratamento de Erros
- Validações @Valid nos DTOs
- GlobalExceptionHandler para padronizar erros
- Mensagens de erro descritivas
- Validação de regras de negócio

### ✅ Paginação e Filtros
- Paginação em todas as listagens
- Filtros opcionais (categoria, status, data)
- Metadados de paginação (total, páginas)
- Links de navegação (first, last, next, prev)

### ✅ Testes de Integração
- Testes com MockMvc para todos os controllers
- RestauranteControllerIT - Cenários completos
- ProdutoControllerIT - Cenários completos
- PedidoControllerIT - Cenários completos
- RelatorioControllerIT - Cenários completos
- Validação de códigos HTTP
- Validação de contratos

### ✅ Collection Postman/Insomnia
- Collection completa com todos os endpoints
- Pasta "Testes de Validação" com cenários de erro
- Variáveis de ambiente configuradas
- Testes automatizados nas requisições
- IDs salvos automaticamente
- Fluxo de teste documentado

### ✅ Testes de Validação
- ValidationErrorIT com 9 cenários de teste
- Testes para dados inválidos (nome vazio, preço negativo, etc.)
- Testes para entidades não encontradas (404)
- Validação de respostas padronizadas (RFC 7807)

## 🧪 Executando Testes

### Testes Unitários e de Integração

```bash
./mvnw test
```

### Testes de Integração Específicos

```bash
# Todos os testes de integração
./mvnw test -Dtest=*ControllerIT

# Teste específico
./mvnw test -Dtest=RestauranteControllerIT
```

### Collection Postman

A collection do Postman está disponível em `postman/Delivery-API.postman_collection.json`.

**Como usar:**
1. Importe a collection no Postman
2. Configure a variável `baseUrl` (padrão: `http://localhost:8080`)
3. Execute as requisições na ordem recomendada
4. Os IDs são salvos automaticamente nas variáveis de ambiente

Veja mais detalhes em `postman/README.md`.

## 📝 Exemplo de Uso

### Criar um Restaurante

```bash
curl -X POST http://localhost:8080/api/restaurantes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Express",
    "categoria": "Pizza",
    "endereco": "Rua das Flores, 123",
    "cep": "01310-100",
    "taxaEntrega": 5.00,
    "tempoEntrega": "30-45 min",
    "ativo": true
  }'
```

### Criar um Pedido

```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "restauranteId": 1,
    "enderecoEntrega": "Rua A, 123",
    "itens": [
      {
        "produtoId": 1,
        "quantidade": 2
      }
    ]
  }'
```

## 🎯 URLs de Acesso

### Desenvolvimento
- **API Base**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console
- **Health**: http://localhost:8080/health

## 🔍 Validações Implementadas

### RestauranteDTO
- Nome: obrigatório, 2-100 caracteres
- Categoria: obrigatória, valores permitidos (Pizza, Hambúrguer, etc.)
- Endereço: obrigatório, 5-200 caracteres
- CEP: formato válido (00000-000 ou 00000000)
- Taxa de entrega: obrigatória, positiva ou zero, máximo R$ 100,00
- Tempo de entrega: formato 30-45 min
- Avaliação: 0.0 a 5.0

### ProdutoDTO
- Nome: obrigatório, 2-50 caracteres
- Descrição: 10-500 caracteres (quando informada)
- Preço: obrigatório, maior que zero, máximo R$ 500,00
- Categoria: obrigatória, valores permitidos
- Restaurante ID: obrigatório, positivo

### PedidoDTO
- Cliente ID: obrigatório, positivo
- Restaurante ID: obrigatório, positivo
- Endereço de entrega: obrigatório, 5-200 caracteres
- Itens: obrigatório, não vazio
- Quantidade por item: positiva, máximo 100 unidades

## 🚨 Tratamento de Erros (RFC 7807)

A API retorna erros padronizados seguindo RFC 7807:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação nos dados enviados",
  "path": "/api/restaurantes",
  "details": {
    "nome": "Nome é obrigatório",
    "preco": "Preço deve ser maior que zero"
  },
  "validationErrors": [
    {
      "field": "nome",
      "message": "Nome é obrigatório",
      "rejectedValue": null
    }
  ]
}
```

### Códigos HTTP Utilizados
- **400 Bad Request**: Dados inválidos ou erros de validação
- **404 Not Found**: Entidade não encontrada
- **409 Conflict**: Conflito de dados (ex: nome duplicado)
- **422 Unprocessable Entity**: Regra de negócio violada
- **500 Internal Server Error**: Erro interno do servidor

## 👨💻 Desenvolvedor

Gabriel de Freitas Monguilhott - Desenvolvido com JDK 21 e Spring Boot 3.5.7

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.
