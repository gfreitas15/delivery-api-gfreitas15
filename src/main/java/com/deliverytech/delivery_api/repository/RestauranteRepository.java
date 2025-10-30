package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByCategoria(String categoria);
    List<Restaurante> findByAtivoTrue();
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT r FROM Restaurante r ORDER BY r.avaliacao DESC")
    List<Restaurante> findAllOrderByAvaliacao();

    List<Restaurante> findTop10ByOrderByAvaliacaoDesc();
}