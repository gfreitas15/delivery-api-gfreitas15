package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByClienteId(Long clienteId);
    
    List<Pedido> findByStatus(String status);
    
    List<Pedido> findTop10ByOrderByDataPedidoDesc();
    
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
    
    @Query(value = "SELECT r.id as restauranteId, r.nome as nomeRestaurante, SUM(ip.subtotal) as totalVendas " +
           "FROM pedidos p " +
           "JOIN itens_pedido ip ON p.id = ip.pedido_id " +
           "JOIN produtos pr ON ip.produto_id = pr.id " +
           "JOIN restaurantes r ON pr.restaurante_id = r.id " +
           "WHERE p.status = 'ENTREGUE' " +
           "GROUP BY r.id, r.nome " +
           "ORDER BY totalVendas DESC", nativeQuery = true)
    List<Object[]> findTotalVendasPorRestaurante();
    
    @Query("SELECT p FROM Pedido p WHERE p.valorTotal >= :valor")
    List<Pedido> findPedidosComValorAcimaDe(@Param("valor") java.math.BigDecimal valor);
    
    @Query("SELECT p FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim AND p.status = :status")
    List<Pedido> findPedidosPorPeriodoEStatus(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("status") String status
    );
}