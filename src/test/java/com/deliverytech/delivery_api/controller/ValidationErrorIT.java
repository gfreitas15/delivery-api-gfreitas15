package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.PedidoDTO;
import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ValidationErrorIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Restaurante restaurante;
    private Cliente cliente;
    private Produto produto;
    
    @BeforeEach
    void setUp() {
        restauranteRepository.deleteAll();
        clienteRepository.deleteAll();
        produtoRepository.deleteAll();
        
        restaurante = new Restaurante();
        restaurante.setNome("Pizza Express");
        restaurante.setCategoria("Pizza");
        restaurante.setEndereco("Rua das Flores, 123");
        restaurante.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante.setAtivo(true);
        restaurante = restauranteRepository.save(restaurante);
        
        cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao@example.com");
        cliente.setTelefone("11999999999");
        cliente.setAtivo(true);
        cliente = clienteRepository.save(cliente);
        
        produto = new Produto();
        produto.setNome("Pizza Margherita");
        produto.setPreco(new BigDecimal("35.90"));
        produto.setCategoria("Pizza");
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        produto = produtoRepository.save(produto);
    }
    
    @Test
    void deveRetornar400QuandoRestauranteComNomeVazio() throws Exception {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome(""); // Nome vazio
        dto.setCategoria("Pizza");
        dto.setEndereco("Rua das Flores, 123");
        dto.setTaxaEntrega(new BigDecimal("5.00"));
        
        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/restaurantes"))
                .andExpect(jsonPath("$.validationErrors").isArray())
                .andExpect(jsonPath("$.validationErrors[?(@.field == 'nome')]").exists());
    }
    
    @Test
    void deveRetornar400QuandoProdutoComPrecoNegativo() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Pizza Teste");
        dto.setPreco(new BigDecimal("-10.00")); // Preço negativo
        dto.setCategoria("Pizza");
        dto.setRestauranteId(restaurante.getId());
        
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors[?(@.field == 'preco')]").exists());
    }
    
    @Test
    void deveRetornar400QuandoProdutoComPrecoAcimaDoMaximo() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Pizza Teste");
        dto.setPreco(new BigDecimal("600.00")); // Acima de R$ 500,00
        dto.setCategoria("Pizza");
        dto.setRestauranteId(restaurante.getId());
        
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors[?(@.field == 'preco')]").exists());
    }
    
    @Test
    void deveRetornar400QuandoPedidoSemItens() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(cliente.getId());
        dto.setRestauranteId(restaurante.getId());
        dto.setEnderecoEntrega("Rua A, 123");
        dto.setItens(null); // Itens nulos
        
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors[?(@.field == 'itens')]").exists());
    }
    
    @Test
    void deveRetornar400QuandoPedidoComListaVazia() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(cliente.getId());
        dto.setRestauranteId(restaurante.getId());
        dto.setEnderecoEntrega("Rua A, 123");
        dto.setItens(java.util.Collections.emptyList()); // Lista vazia
        
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors[?(@.field == 'itens')]").exists());
    }
    
    @Test
    void deveRetornar400QuandoRestauranteComCategoriaInvalida() throws Exception {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Restaurante Teste");
        dto.setCategoria("Categoria Inexistente"); // Categoria inválida
        dto.setEndereco("Rua das Flores, 123");
        dto.setTaxaEntrega(new BigDecimal("5.00"));
        
        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors[?(@.field == 'categoria')]").exists());
    }
    
    @Test
    void deveRetornar400QuandoRestauranteComCEPinvalido() throws Exception {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Restaurante Teste");
        dto.setCategoria("Pizza");
        dto.setEndereco("Rua das Flores, 123");
        dto.setCep("12345"); // CEP inválido
        dto.setTaxaEntrega(new BigDecimal("5.00"));
        
        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors[?(@.field == 'cep')]").exists());
    }
    
    @Test
    void deveRetornar400QuandoProdutoComDescricaoMuitoCurta() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Pizza Teste");
        dto.setDescricao("Curta"); // Menos de 10 caracteres
        dto.setPreco(new BigDecimal("35.90"));
        dto.setCategoria("Pizza");
        dto.setRestauranteId(restaurante.getId());
        
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors[?(@.field == 'descricao')]").exists());
    }
    
    @Test
    void deveRetornar404QuandoRestauranteNaoExiste() throws Exception {
        mockMvc.perform(post("/api/restaurantes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").exists());
    }
}

