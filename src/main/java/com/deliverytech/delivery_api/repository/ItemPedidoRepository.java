package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
    List<ItemPedido> findByPedidoId(Long pedidoId);
    
    List<ItemPedido> findByProdutoId(Long produtoId);
    
    @Query(value = "SELECT p.id as produtoId, p.nome as nomeProduto, SUM(ip.quantidade) as quantidadeTotal " +
           "FROM itens_pedido ip " +
           "JOIN produtos p ON ip.produto_id = p.id " +
           "GROUP BY p.id, p.nome " +
           "ORDER BY quantidadeTotal DESC " +
           "LIMIT :limit", nativeQuery = true)
    List<Object[]> findProdutosMaisVendidos(@Param("limit") int limit);
    
    @Query(value = "SELECT c.id as clienteId, c.nome as nomeCliente, COUNT(p.id) as totalPedidos " +
           "FROM pedidos p " +
           "JOIN clientes c ON p.cliente_id = c.id " +
           "GROUP BY c.id, c.nome " +
           "ORDER BY totalPedidos DESC " +
           "LIMIT :limit", nativeQuery = true)
    List<Object[]> findRankingClientesPorPedidos(@Param("limit") int limit);
    
    @Query(value = "SELECT r.categoria, SUM(p.valor_total) as faturamentoTotal " +
           "FROM pedidos p " +
           "JOIN itens_pedido ip ON p.id = ip.pedido_id " +
           "JOIN produtos pr ON ip.produto_id = pr.id " +
           "JOIN restaurantes r ON pr.restaurante_id = r.id " +
           "WHERE p.status = 'ENTREGUE' " +
           "GROUP BY r.categoria " +
           "ORDER BY faturamentoTotal DESC", nativeQuery = true)
    List<Object[]> findFaturamentoPorCategoria();
}

