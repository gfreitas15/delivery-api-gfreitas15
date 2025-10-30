# Delivery API

API de exemplo para gestão de clientes, restaurantes, produtos e pedidos usando Spring Boot 3, JDK 21 e H2.

## Requisitos
- JDK 21
- Maven 3.9+

## Como executar
```bash
mvn spring-boot:run
```
- Base URL: `http://localhost:8080`
- Console H2: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:deliverydb`
  - User: `sa` / Password: vazio

Dados de exemplo são carregados via `src/main/resources/data.sql`.

## Endpoints principais

### Health
- GET `/health`
- GET `/info`

### Clientes
- POST `/clientes`
- GET `/clientes`
- GET `/clientes/ativos`
- GET `/clientes/{id}`
- PUT `/clientes/{id}`
- DELETE `/clientes/{id}` (inativa)

### Restaurantes
- POST `/restaurantes`
- GET `/restaurantes` (filtros: `categoria`, `ativos=true`)
- GET `/restaurantes/top-avaliacao`
- GET `/restaurantes/{id}`
- PUT `/restaurantes/{id}`
- POST `/restaurantes/{id}/ativar`
- POST `/restaurantes/{id}/inativar`
- DELETE `/restaurantes/{id}`

### Produtos
- POST `/restaurantes/{restauranteId}/produtos`
- GET `/restaurantes/{restauranteId}/produtos` (filtro: `disponivel=true`)
- GET `/produtos` (filtros: `categoria`, `disponivel=true`)
- PUT `/produtos/{id}`
- DELETE `/produtos/{id}`

### Pedidos
- POST `/pedidos?clienteId={id}`
- GET `/pedidos/{id}`
- GET `/clientes/{clienteId}/pedidos`
- GET `/pedidos?status=...` ou `/pedidos?inicio=YYYY-MM-DD&fim=YYYY-MM-DD`
- PUT `/pedidos/{id}/status?status=...`

## Postman
Coleção em `postman/DeliveryAPI.postman_collection.json` com exemplos prontos.

## Configurações úteis
- `application.properties` já habilita H2 console e `spring.jpa.hibernate.ddl-auto=create-drop` (para demo). Em produção, altere para `update` ou gerencie via migrações.
