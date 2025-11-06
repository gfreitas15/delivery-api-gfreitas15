# Collection do Postman - Delivery API

## O que é uma Collection do Postman?

Uma Collection do Postman é um arquivo JSON que contém todos os endpoints da API organizados em pastas, permitindo testar a API sem precisar digitar manualmente cada requisição.

## Como usar a Collection

### 1. Importar no Postman

1. Abra o Postman
2. Clique em **Import** (canto superior esquerdo)
3. Selecione o arquivo `postman/DeliveryAPI.postman_collection.json`
4. A collection será importada com todos os endpoints organizados

### 2. Testar os Endpoints

A collection está organizada em pastas:

- **Health** - Endpoints de saúde da aplicação
- **Clientes** - CRUD completo de clientes
- **Restaurantes** - CRUD completo de restaurantes
- **Produtos** - CRUD completo de produtos
- **Pedidos** - Operações complexas de pedidos

### 3. Executar Requisições

1. Selecione uma requisição na collection
2. Clique em **Send** para executar
3. Veja a resposta no painel inferior

## Endpoints Principais

### Clientes
- `POST /api/clientes` - Cadastrar cliente
- `GET /api/clientes` - Listar clientes ativos
- `GET /api/clientes/{id}` - Buscar por ID
- `GET /api/clientes/email/{email}` - Buscar por email
- `PUT /api/clientes/{id}` - Atualizar cliente
- `PATCH /api/clientes/{id}/status` - Ativar/desativar

### Restaurantes
- `POST /api/restaurantes` - Cadastrar restaurante
- `GET /api/restaurantes` - Listar disponíveis
- `GET /api/restaurantes/{id}` - Buscar por ID
- `GET /api/restaurantes/categoria/{categoria}` - Por categoria
- `PUT /api/restaurantes/{id}` - Atualizar restaurante
- `GET /api/restaurantes/{id}/taxa-entrega/{cep}` - Calcular taxa

### Produtos
- `POST /api/produtos` - Cadastrar produto
- `GET /api/produtos/{id}` - Buscar por ID
- `GET /api/restaurantes/{restauranteId}/produtos` - Produtos do restaurante
- `PUT /api/produtos/{id}` - Atualizar produto
- `PATCH /api/produtos/{id}/disponibilidade` - Alterar disponibilidade
- `GET /api/produtos/categoria/{categoria}` - Por categoria

### Pedidos
- `POST /api/pedidos` - Criar pedido completo (transação complexa)
- `GET /api/pedidos/{id}` - Buscar pedido completo
- `GET /api/clientes/{clienteId}/pedidos` - Histórico do cliente
- `PATCH /api/pedidos/{id}/status` - Atualizar status
- `DELETE /api/pedidos/{id}` - Cancelar pedido
- `POST /api/pedidos/calcular` - Calcular total sem salvar

## Cenários de Teste

### Cenário 1: Cadastro de Cliente
```
POST /api/clientes
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "11999999999",
  "endereco": "Rua A, 123"
}
```
Resultado Esperado: Status 201 com cliente criado

### Cenário 2: Criação de Pedido Completo
```
POST /api/pedidos
{
  "clienteId": 1,
  "restauranteId": 1,
  "enderecoEntrega": "Rua B, 456",
  "itens": [
    {"produtoId": 1, "quantidade": 2},
    {"produtoId": 2, "quantidade": 1}
  ]
}
```
Resultado Esperado: Status 201 com pedido criado e total calculado

### Cenário 3: Busca de Produtos por Restaurante
```
GET /api/restaurantes/1/produtos
```
Resultado Esperado: Lista de produtos disponíveis do restaurante

### Cenário 4: Atualização de Status do Pedido
```
PATCH /api/pedidos/1/status
{
  "status": "EM_PREPARO"
}
```
Resultado Esperado: Status atualizado com validação

## Entrega via GitHub

Para entregar o projeto via GitHub:

1. **Fazer commit da collection:**
```bash
git add postman/DeliveryAPI.postman_collection.json
git commit -m "feat: add Postman collection with all API endpoints"
```

2. **Fazer push para o repositório:**
```bash
git push origin main
```

3. **A collection estará disponível no repositório** para download e uso

## Variáveis da Collection

A collection possui uma variável `base_url` configurada para `http://localhost:8080`. Você pode alterar isso no Postman se necessário.

