package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(String status);
    List<Pedido> findByDataPedidoBetween(LocalDate inicio, LocalDate fim);

    @Query("SELECT p FROM Pedido p WHERE p.status = 'ENTREGUE'")
    List<Pedido> findEntregues();

    List<Pedido> findByClienteIdAndStatus(Long clienteId, String status);

    @Query("SELECT p FROM Pedido p WHERE p.status = 'ENTREGUE' AND p.dataPedido BETWEEN :inicio AND :fim")
    List<Pedido> findEntreguesBetween(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}