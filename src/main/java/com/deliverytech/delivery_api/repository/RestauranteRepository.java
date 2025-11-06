package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    List<Restaurante> findByCategoria(String categoria);
    
    List<Restaurante> findByAtivoTrue();
    
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);
    
    List<Restaurante> findTop5ByOrderByNomeAsc();
}