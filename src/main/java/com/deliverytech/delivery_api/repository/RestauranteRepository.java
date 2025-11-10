package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    Page<Restaurante> findByCategoriaAndAtivo(String categoria, Boolean ativo, Pageable pageable);
    Page<Restaurante> findByAtivo(Boolean ativo, Pageable pageable);
    List<Restaurante> findByAtivo(Boolean ativo);
    List<Restaurante> findByCategoria(String categoria);
    boolean existsByNome(String nome);
}

