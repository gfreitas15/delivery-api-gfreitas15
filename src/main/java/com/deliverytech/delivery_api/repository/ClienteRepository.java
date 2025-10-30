package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT c FROM Cliente c WHERE c.ativo = true")
    List<Cliente> findAtivos();
}