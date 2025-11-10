package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestauranteService {
    
    private final RestauranteRepository repository;
    
    @Transactional
    public RestauranteResponseDTO cadastrar(RestauranteDTO dto) {
        if (repository.existsByNome(dto.getNome())) {
            throw new BusinessException("Restaurante com este nome já existe");
        }
        
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setCep(dto.getCep());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega() != null ? dto.getTaxaEntrega() : BigDecimal.ZERO);
        restaurante.setTempoEntrega(dto.getTempoEntrega());
        restaurante.setAvaliacao(dto.getAvaliacao());
        restaurante.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        
        restaurante = repository.save(restaurante);
        return toResponseDTO(restaurante);
    }
    
    @Transactional(readOnly = true)
    public Page<RestauranteResponseDTO> listar(String categoria, Boolean ativo, Pageable pageable) {
        Page<Restaurante> restaurantes;
        
        if (categoria != null && ativo != null) {
            restaurantes = repository.findByCategoriaAndAtivo(categoria, ativo, pageable);
        } else if (ativo != null) {
            restaurantes = repository.findByAtivo(ativo, pageable);
        } else {
            restaurantes = repository.findAll(pageable);
        }
        
        return restaurantes.map(this::toResponseDTO);
    }
    
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarPorId(Long id) {
        Restaurante restaurante = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + id));
        return toResponseDTO(restaurante);
    }
    
    @Transactional
    public RestauranteResponseDTO atualizar(Long id, RestauranteDTO dto) {
        Restaurante restaurante = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + id));
        
        if (!restaurante.getNome().equals(dto.getNome()) && repository.existsByNome(dto.getNome())) {
            throw new BusinessException("Restaurante com este nome já existe");
        }
        
        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setCep(dto.getCep());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega() != null ? dto.getTaxaEntrega() : BigDecimal.ZERO);
        restaurante.setTempoEntrega(dto.getTempoEntrega());
        restaurante.setAvaliacao(dto.getAvaliacao());
        if (dto.getAtivo() != null) {
            restaurante.setAtivo(dto.getAtivo());
        }
        
        restaurante = repository.save(restaurante);
        return toResponseDTO(restaurante);
    }
    
    @Transactional
    public void atualizarStatus(Long id, Boolean ativo) {
        Restaurante restaurante = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + id));
        restaurante.setAtivo(ativo);
        repository.save(restaurante);
    }
    
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        List<Restaurante> restaurantes = repository.findByCategoria(categoria);
        return restaurantes.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public BigDecimal calcularTaxaEntrega(Long id, String cep) {
        Restaurante restaurante = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + id));
        // Lógica simplificada - em produção, calcularia baseado na distância
        return restaurante.getTaxaEntrega();
    }
    
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarProximos(String cep) {
        // Lógica simplificada - em produção, calcularia distância baseada no CEP
        List<Restaurante> restaurantes = repository.findByAtivo(true);
        return restaurantes.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
    
    private RestauranteResponseDTO toResponseDTO(Restaurante restaurante) {
        return new RestauranteResponseDTO(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getCategoria(),
            restaurante.getEndereco(),
            restaurante.getCep(),
            restaurante.getTaxaEntrega(),
            restaurante.getTempoEntrega(),
            restaurante.getAvaliacao(),
            restaurante.getAtivo(),
            restaurante.getDataCadastro()
        );
    }
}

