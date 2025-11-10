package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.*;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProdutoRepository produtoRepository;
    
    @Transactional
    public PedidoResponseDTO criar(PedidoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));
        
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + dto.getRestauranteId()));
        
        if (!restaurante.getAtivo()) {
            throw new BusinessException("Restaurante está inativo");
        }
        
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setStatus(Pedido.StatusPedido.PENDENTE);
        
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (PedidoDTO.ItemPedidoDTO itemDTO : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));
            
            if (!produto.getDisponivel()) {
                throw new BusinessException("Produto " + produto.getNome() + " não está disponível");
            }
            
            if (!produto.getRestaurante().getId().equals(restaurante.getId())) {
                throw new BusinessException("Produto não pertence ao restaurante selecionado");
            }
            
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));
            
            pedido.getItens().add(item);
            subtotal = subtotal.add(item.getSubtotal());
        }
        
        pedido.setSubtotal(subtotal);
        pedido.setTaxaEntrega(restaurante.getTaxaEntrega());
        pedido.setTotal(subtotal.add(restaurante.getTaxaEntrega()));
        
        pedido = pedidoRepository.save(pedido);
        return toResponseDTO(pedido);
    }
    
    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
        return toResponseDTO(pedido);
    }
    
    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listar(Pedido.StatusPedido status, Pageable pageable) {
        Page<Pedido> pedidos;
        if (status != null) {
            pedidos = pedidoRepository.findByStatus(status, pageable);
        } else {
            pedidos = pedidoRepository.findAll(pageable);
        }
        return pedidos.map(this::toResponseDTO);
    }
    
    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, Pedido.StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
        
        if (pedido.getStatus() == Pedido.StatusPedido.CANCELADO) {
            throw new BusinessException("Pedido cancelado não pode ter status alterado");
        }
        
        if (pedido.getStatus() == Pedido.StatusPedido.ENTREGUE) {
            throw new BusinessException("Pedido entregue não pode ter status alterado");
        }
        
        pedido.setStatus(novoStatus);
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedido = pedidoRepository.save(pedido);
        return toResponseDTO(pedido);
    }
    
    @Transactional
    public void cancelar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
        
        if (pedido.getStatus() == Pedido.StatusPedido.ENTREGUE) {
            throw new BusinessException("Pedido entregue não pode ser cancelado");
        }
        
        if (pedido.getStatus() == Pedido.StatusPedido.CANCELADO) {
            throw new BusinessException("Pedido já está cancelado");
        }
        
        pedido.setStatus(Pedido.StatusPedido.CANCELADO);
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedidoRepository.save(pedido);
    }
    
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPorCliente(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        return pedidos.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPorRestaurante(Long restauranteId) {
        List<Pedido> pedidos = pedidoRepository.findByRestauranteId(restauranteId);
        return pedidos.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CalcularPedidoResponseDTO calcular(CalcularPedidoDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + dto.getRestauranteId()));
        
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (CalcularPedidoDTO.ItemPedidoDTO itemDTO : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));
            
            if (!produto.getRestaurante().getId().equals(restaurante.getId())) {
                throw new BusinessException("Produto não pertence ao restaurante selecionado");
            }
            
            BigDecimal itemSubtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
            subtotal = subtotal.add(itemSubtotal);
        }
        
        BigDecimal taxaEntrega = restaurante.getTaxaEntrega();
        BigDecimal total = subtotal.add(taxaEntrega);
        
        return new CalcularPedidoResponseDTO(subtotal, taxaEntrega, total);
    }
    
    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        List<PedidoResponseDTO.ItemPedidoResponseDTO> itensDTO = pedido.getItens().stream()
            .map(item -> new PedidoResponseDTO.ItemPedidoResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
            ))
            .collect(Collectors.toList());
        
        return new PedidoResponseDTO(
            pedido.getId(),
            pedido.getCliente().getId(),
            pedido.getCliente().getNome(),
            pedido.getRestaurante().getId(),
            pedido.getRestaurante().getNome(),
            pedido.getEnderecoEntrega(),
            pedido.getStatus(),
            pedido.getSubtotal(),
            pedido.getTaxaEntrega(),
            pedido.getTotal(),
            itensDTO,
            pedido.getDataCriacao(),
            pedido.getDataAtualizacao()
        );
    }
}

