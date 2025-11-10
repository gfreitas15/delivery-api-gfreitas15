package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.PedidoDTO;
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PedidoControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;
    
    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();
        clienteRepository.deleteAll();
        
        cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao@example.com");
        cliente.setTelefone("11999999999");
        cliente.setAtivo(true);
        cliente = clienteRepository.save(cliente);
        
        restaurante = new Restaurante();
        restaurante.setNome("Pizza Express");
        restaurante.setCategoria("Pizza");
        restaurante.setEndereco("Rua das Flores, 123");
        restaurante.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante.setAtivo(true);
        restaurante = restauranteRepository.save(restaurante);
        
        produto = new Produto();
        produto.setNome("Pizza Margherita");
        produto.setPreco(new BigDecimal("35.90"));
        produto.setCategoria("Pizza");
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        produto = produtoRepository.save(produto);
    }
    
    @Test
    void deveCriarPedido() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(cliente.getId());
        dto.setRestauranteId(restaurante.getId());
        dto.setEnderecoEntrega("Rua A, 123");
        
        List<PedidoDTO.ItemPedidoDTO> itens = new ArrayList<>();
        PedidoDTO.ItemPedidoDTO item = new PedidoDTO.ItemPedidoDTO();
        item.setProdutoId(produto.getId());
        item.setQuantidade(2);
        itens.add(item);
        dto.setItens(itens);
        
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("PENDENTE"))
                .andExpect(jsonPath("$.data.itens").isArray())
                .andExpect(jsonPath("$.data.itens[0].quantidade").value(2));
    }
    
    @Test
    void deveRetornar404QuandoClienteNaoExiste() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(999L);
        dto.setRestauranteId(restaurante.getId());
        dto.setEnderecoEntrega("Rua A, 123");
        
        List<PedidoDTO.ItemPedidoDTO> itens = new ArrayList<>();
        PedidoDTO.ItemPedidoDTO item = new PedidoDTO.ItemPedidoDTO();
        item.setProdutoId(produto.getId());
        item.setQuantidade(1);
        itens.add(item);
        dto.setItens(itens);
        
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        // DTO sem campos obrigatórios
        
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}

