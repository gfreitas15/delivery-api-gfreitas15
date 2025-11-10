package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;
    
    @Transactional
    public ProdutoResponseDTO cadastrar(ProdutoDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + dto.getRestauranteId()));
        
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());
        produto.setDisponivel(dto.getDisponivel() != null ? dto.getDisponivel() : true);
        produto.setRestaurante(restaurante);
        
        produto = produtoRepository.save(produto);
        return toResponseDTO(produto);
    }
    
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
        return toResponseDTO(produto);
    }
    
    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
        
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + dto.getRestauranteId()));
        
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());
        produto.setRestaurante(restaurante);
        if (dto.getDisponivel() != null) {
            produto.setDisponivel(dto.getDisponivel());
        }
        
        produto = produtoRepository.save(produto);
        return toResponseDTO(produto);
    }
    
    @Transactional
    public void remover(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
        produtoRepository.delete(produto);
    }
    
    @Transactional
    public void toggleDisponibilidade(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
        produto.setDisponivel(!produto.getDisponivel());
        produtoRepository.save(produto);
    }
    
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorRestaurante(Long restauranteId, Boolean disponivel) {
        List<Produto> produtos;
        if (disponivel != null) {
            produtos = produtoRepository.findByRestauranteIdAndDisponivel(restauranteId, disponivel);
        } else {
            produtos = produtoRepository.findByRestauranteId(restauranteId);
        }
        return produtos.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorCategoria(String categoria) {
        List<Produto> produtos = produtoRepository.findByCategoria(categoria);
        return produtos.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);
        return produtos.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
    
    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getCategoria(),
            produto.getDisponivel(),
            produto.getRestaurante().getId(),
            produto.getRestaurante().getNome(),
            produto.getDataCadastro()
        );
    }
}

