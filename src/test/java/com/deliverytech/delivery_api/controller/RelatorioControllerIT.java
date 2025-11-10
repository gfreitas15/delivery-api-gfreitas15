package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RelatorioControllerIT {
    
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
    
    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;
    private Pedido pedido;
    
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
        
        pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega("Rua A, 123");
        pedido.setStatus(Pedido.StatusPedido.CONFIRMADO);
        pedido.setSubtotal(new BigDecimal("71.80"));
        pedido.setTaxaEntrega(new BigDecimal("5.00"));
        pedido.setTotal(new BigDecimal("76.80"));
        
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(2);
        item.setPrecoUnitario(new BigDecimal("35.90"));
        item.setSubtotal(new BigDecimal("71.80"));
        pedido.getItens().add(item);
        
        pedido = pedidoRepository.save(pedido);
    }
    
    @Test
    void deveRetornarVendasPorRestaurante() throws Exception {
        mockMvc.perform(get("/api/relatorios/vendas-por-restaurante"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].restauranteId").value(restaurante.getId()));
    }
    
    @Test
    void deveRetornarVendasPorRestauranteComPeriodo() throws Exception {
        LocalDateTime inicio = LocalDateTime.now().minusDays(30);
        LocalDateTime fim = LocalDateTime.now().plusDays(1);
        
        mockMvc.perform(get("/api/relatorios/vendas-por-restaurante")
                .param("dataInicio", inicio.toString())
                .param("dataFim", fim.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveRetornarProdutosMaisVendidos() throws Exception {
        mockMvc.perform(get("/api/relatorios/produtos-mais-vendidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveRetornarProdutosMaisVendidosComLimite() throws Exception {
        mockMvc.perform(get("/api/relatorios/produtos-mais-vendidos")
                .param("limite", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveRetornarClientesAtivos() throws Exception {
        mockMvc.perform(get("/api/relatorios/clientes-ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].clienteId").value(cliente.getId()));
    }
    
    @Test
    void deveRetornarClientesAtivosComLimite() throws Exception {
        mockMvc.perform(get("/api/relatorios/clientes-ativos")
                .param("limite", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveRetornarPedidosPorPeriodo() throws Exception {
        mockMvc.perform(get("/api/relatorios/pedidos-por-periodo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveRetornarPedidosPorPeriodoComDatas() throws Exception {
        LocalDateTime inicio = LocalDateTime.now().minusDays(30);
        LocalDateTime fim = LocalDateTime.now().plusDays(1);
        
        mockMvc.perform(get("/api/relatorios/pedidos-por-periodo")
                .param("dataInicio", inicio.toString())
                .param("dataFim", fim.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
}

