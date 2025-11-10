package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    
    private final PedidoRepository pedidoRepository;
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> vendasPorRestaurante(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Pedido> pedidos;
        
        if (dataInicio != null && dataFim != null) {
            pedidos = pedidoRepository.findByDataCriacaoBetween(dataInicio, dataFim);
        } else {
            pedidos = pedidoRepository.findAll();
        }
        
        Map<Long, Map<String, Object>> vendasMap = new HashMap<>();
        
        for (Pedido pedido : pedidos) {
            if (pedido.getStatus() == Pedido.StatusPedido.CANCELADO) {
                continue;
            }
            
            Long restauranteId = pedido.getRestaurante().getId();
            String restauranteNome = pedido.getRestaurante().getNome();
            
            vendasMap.computeIfAbsent(restauranteId, k -> {
                Map<String, Object> venda = new HashMap<>();
                venda.put("restauranteId", restauranteId);
                venda.put("restauranteNome", restauranteNome);
                venda.put("totalVendas", BigDecimal.ZERO);
                venda.put("quantidadePedidos", 0);
                return venda;
            });
            
            Map<String, Object> venda = vendasMap.get(restauranteId);
            venda.put("totalVendas", ((BigDecimal) venda.get("totalVendas")).add(pedido.getTotal()));
            venda.put("quantidadePedidos", (Integer) venda.get("quantidadePedidos") + 1);
        }
        
        return new ArrayList<>(vendasMap.values());
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> produtosMaisVendidos(Integer limite) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        Map<Long, Map<String, Object>> produtosMap = new HashMap<>();
        
        for (Pedido pedido : pedidos) {
            if (pedido.getStatus() == Pedido.StatusPedido.CANCELADO) {
                continue;
            }
            
            for (var item : pedido.getItens()) {
                Long produtoId = item.getProduto().getId();
                String produtoNome = item.getProduto().getNome();
                
                produtosMap.computeIfAbsent(produtoId, k -> {
                    Map<String, Object> produto = new HashMap<>();
                    produto.put("produtoId", produtoId);
                    produto.put("produtoNome", produtoNome);
                    produto.put("quantidadeVendida", 0);
                    produto.put("totalVendido", BigDecimal.ZERO);
                    return produto;
                });
                
                Map<String, Object> produto = produtosMap.get(produtoId);
                produto.put("quantidadeVendida", (Integer) produto.get("quantidadeVendida") + item.getQuantidade());
                produto.put("totalVendido", ((BigDecimal) produto.get("totalVendido")).add(item.getSubtotal()));
            }
        }
        
        List<Map<String, Object>> produtos = new ArrayList<>(produtosMap.values());
        produtos.sort((a, b) -> ((Integer) b.get("quantidadeVendida")).compareTo((Integer) a.get("quantidadeVendida")));
        
        if (limite != null && limite > 0) {
            produtos = produtos.stream().limit(limite).collect(Collectors.toList());
        }
        
        return produtos;
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> clientesAtivos(Integer limite) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        Map<Long, Map<String, Object>> clientesMap = new HashMap<>();
        
        for (Pedido pedido : pedidos) {
            if (pedido.getStatus() == Pedido.StatusPedido.CANCELADO) {
                continue;
            }
            
            Long clienteId = pedido.getCliente().getId();
            String clienteNome = pedido.getCliente().getNome();
            
            clientesMap.computeIfAbsent(clienteId, k -> {
                Map<String, Object> cliente = new HashMap<>();
                cliente.put("clienteId", clienteId);
                cliente.put("clienteNome", clienteNome);
                cliente.put("totalPedidos", 0);
                cliente.put("totalGasto", BigDecimal.ZERO);
                return cliente;
            });
            
            Map<String, Object> cliente = clientesMap.get(clienteId);
            cliente.put("totalPedidos", (Integer) cliente.get("totalPedidos") + 1);
            cliente.put("totalGasto", ((BigDecimal) cliente.get("totalGasto")).add(pedido.getTotal()));
        }
        
        List<Map<String, Object>> clientes = new ArrayList<>(clientesMap.values());
        clientes.sort((a, b) -> ((Integer) b.get("totalPedidos")).compareTo((Integer) a.get("totalPedidos")));
        
        if (limite != null && limite > 0) {
            clientes = clientes.stream().limit(limite).collect(Collectors.toList());
        }
        
        return clientes;
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> pedidosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Pedido> pedidos;
        
        if (dataInicio != null && dataFim != null) {
            pedidos = pedidoRepository.findByDataCriacaoBetween(dataInicio, dataFim);
        } else {
            pedidos = pedidoRepository.findAll();
        }
        
        return pedidos.stream().map(pedido -> {
            Map<String, Object> pedidoMap = new HashMap<>();
            pedidoMap.put("pedidoId", pedido.getId());
            pedidoMap.put("clienteNome", pedido.getCliente().getNome());
            pedidoMap.put("restauranteNome", pedido.getRestaurante().getNome());
            pedidoMap.put("status", pedido.getStatus().toString());
            pedidoMap.put("total", pedido.getTotal());
            pedidoMap.put("dataCriacao", pedido.getDataCriacao());
            return pedidoMap;
        }).collect(Collectors.toList());
    }
}

