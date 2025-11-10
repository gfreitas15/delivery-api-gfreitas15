# Collection Postman - Delivery Tech API

Esta pasta contém a collection do Postman com todos os endpoints da API REST de Delivery Tech.

## 📦 Arquivos

- `Delivery-API.postman_collection.json` - Collection completa com todos os endpoints
- `Delivery-API.postman_environment.json` - Ambiente do Postman com variáveis pré-configuradas

## 🚀 Como usar

### 1. Importar a Collection

1. Abra o Postman
2. Clique em **Import**
3. Selecione o arquivo `Delivery-API.postman_collection.json`
4. A collection será importada com todas as requisições organizadas

### 2. Importar o Ambiente (Opcional)

Para facilitar, você pode importar o arquivo de ambiente:

1. No Postman, clique em **Import**
2. Selecione o arquivo `Delivery-API.postman_environment.json`
3. O ambiente será importado com todas as variáveis pré-configuradas

### 3. Configurar Variáveis de Ambiente

A collection usa variáveis para facilitar os testes:

- `baseUrl` - URL base da API (padrão: `http://localhost:8080`)
- `restauranteId` - ID do restaurante (será preenchido automaticamente após criar um restaurante)
- `produtoId` - ID do produto (será preenchido automaticamente após criar um produto)
- `clienteId` - ID do cliente (será preenchido automaticamente após criar um cliente)
- `pedidoId` - ID do pedido (será preenchido automaticamente após criar um pedido)

**Para configurar manualmente:**

1. Clique no ícone de **olho** no canto superior direito do Postman
2. Selecione **Environment** ou crie um novo
3. Adicione as variáveis conforme necessário

**Nota:** Se você importou o ambiente, selecione-o no dropdown de ambientes no canto superior direito.

### 4. Executar Requisições

A collection está organizada em pastas:

- **Health & Info** - Endpoints de saúde e informações
- **Restaurantes** - Todas as operações de restaurantes
- **Produtos** - Todas as operações de produtos
- **Pedidos** - Todas as operações de pedidos
- **Relatórios** - Endpoints de relatórios

### 5. Testes Automatizados

Algumas requisições possuem testes automatizados que:
- Verificam o status code da resposta
- Salvam IDs automaticamente nas variáveis de ambiente
- Validam a estrutura da resposta

**Exemplo:** Ao criar um restaurante, o ID será automaticamente salvo na variável `restauranteId` e usado nas próximas requisições.

## 📋 Fluxo de Teste Recomendado

**Nota:** Antes de começar, certifique-se de criar um Cliente primeiro, pois os pedidos precisam de um cliente. Você pode criar um cliente manualmente ou usar o endpoint de clientes (se existir).

1. **Health Check** - Verificar se a API está rodando
2. **Cadastrar Restaurante** - Criar um restaurante (ID será salvo automaticamente)
3. **Cadastrar Produto** - Criar um produto associado ao restaurante
4. **Criar Pedido** - Criar um pedido com o restaurante e produto criados
5. **Atualizar Status** - Atualizar o status do pedido
6. **Relatórios** - Consultar relatórios de vendas


## 🔧 Configuração para Produção

Para testar em produção, altere a variável `baseUrl` para:
```
https://api.deliverytech.com.br
```

## 📝 Notas

- Os IDs são salvos automaticamente após criar recursos
- Certifique-se de que a API está rodando antes de executar as requisições
- Alguns endpoints requerem que outros recursos sejam criados primeiro (ex: produto precisa de restaurante)

## 🧪 Executar Collection Completa

Para executar toda a collection de uma vez:

1. Clique com o botão direito na collection
2. Selecione **Run collection**
3. Configure as opções de execução
4. Clique em **Run Delivery Tech API**

Isso executará todas as requisições em sequência e mostrará os resultados.

