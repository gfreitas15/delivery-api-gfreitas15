INSERT INTO clientes (nome, email, ativo) VALUES ('Ana Souza', 'ana@delivery.com', TRUE);
INSERT INTO clientes (nome, email, ativo) VALUES ('Bruno Lima', 'bruno@delivery.com', TRUE);
INSERT INTO clientes (nome, email, ativo) VALUES ('Carlos Mendes', 'carlos@delivery.com', FALSE);

INSERT INTO restaurantes (nome, categoria, ativo, avaliacao, taxa_entrega) VALUES ('Sabor Caseiro', 'Brasileira', TRUE, 4.5, 5.00);
INSERT INTO restaurantes (nome, categoria, ativo, avaliacao, taxa_entrega) VALUES ('La Pasta', 'Italiana', TRUE, 4.8, 6.50);
INSERT INTO restaurantes (nome, categoria, ativo, avaliacao, taxa_entrega) VALUES ('Veg&Go', 'Vegana', TRUE, 4.2, 4.00);

INSERT INTO produtos (nome, preco, categoria, disponivel, restaurante_id) VALUES ('Feijoada', 49.90, 'Prato Principal', TRUE, 1);
INSERT INTO produtos (nome, preco, categoria, disponivel, restaurante_id) VALUES ('Lasanha Bolonhesa', 39.90, 'Massa', TRUE, 2);
INSERT INTO produtos (nome, preco, categoria, disponivel, restaurante_id) VALUES ('Salada Vegana', 29.90, 'Saudável', TRUE, 3);
INSERT INTO produtos (nome, preco, categoria, disponivel, restaurante_id) VALUES ('Bruschetta', 19.90, 'Entrada', TRUE, 2);
INSERT INTO produtos (nome, preco, categoria, disponivel, restaurante_id) VALUES ('Sucos Naturais', 12.00, 'Bebida', TRUE, 3);

INSERT INTO pedidos (cliente_id, status, data_pedido, valor_subtotal, valor_entrega, valor_total)
VALUES (1, 'PENDENTE', CURRENT_TIMESTAMP, 59.90, 7.90, 67.80);


