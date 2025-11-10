package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProdutoControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Restaurante restaurante;
    private Produto produto;
    
    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();
        
        restaurante = new Restaurante();
        restaurante.setNome("Pizza Express");
        restaurante.setCategoria("Pizza");
        restaurante.setEndereco("Rua das Flores, 123");
        restaurante.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante.setAtivo(true);
        restaurante = restauranteRepository.save(restaurante);
        
        produto = new Produto();
        produto.setNome("Pizza Margherita");
        produto.setDescricao("Pizza com molho de tomate, mussarela e manjericão");
        produto.setPreco(new BigDecimal("35.90"));
        produto.setCategoria("Pizza");
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        produto = produtoRepository.save(produto);
    }
    
    @Test
    void deveCadastrarProduto() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Pizza Calabresa");
        dto.setDescricao("Pizza com calabresa");
        dto.setPreco(new BigDecimal("38.90"));
        dto.setCategoria("Pizza");
        dto.setRestauranteId(restaurante.getId());
        dto.setDisponivel(true);
        
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nome").value("Pizza Calabresa"));
    }
    
    @Test
    void deveBuscarProdutoPorId() throws Exception {
        mockMvc.perform(get("/api/produtos/{id}", produto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(produto.getId()))
                .andExpect(jsonPath("$.data.nome").value("Pizza Margherita"));
    }
    
    @Test
    void deveRetornar404QuandoProdutoNaoExiste() throws Exception {
        mockMvc.perform(get("/api/produtos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
    
    @Test
    void deveAtualizarProduto() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Pizza Margherita Atualizada");
        dto.setDescricao("Pizza atualizada");
        dto.setPreco(new BigDecimal("40.00"));
        dto.setCategoria("Pizza");
        dto.setRestauranteId(restaurante.getId());
        dto.setDisponivel(true);
        
        mockMvc.perform(put("/api/produtos/{id}", produto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nome").value("Pizza Margherita Atualizada"));
    }
    
    @Test
    void deveRemoverProduto() throws Exception {
        mockMvc.perform(delete("/api/produtos/{id}", produto.getId()))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deveToggleDisponibilidade() throws Exception {
        mockMvc.perform(patch("/api/produtos/{id}/disponibilidade", produto.getId()))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deveBuscarProdutosPorRestaurante() throws Exception {
        mockMvc.perform(get("/api/restaurantes/{restauranteId}/produtos", restaurante.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].nome").value("Pizza Margherita"));
    }
    
    @Test
    void deveBuscarProdutosPorCategoria() throws Exception {
        mockMvc.perform(get("/api/produtos/categoria/Pizza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveBuscarProdutosPorNome() throws Exception {
        mockMvc.perform(get("/api/produtos/buscar")
                .param("nome", "Margherita"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
    
    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        // DTO sem campos obrigatórios
        
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
    
    @Test
    void deveRetornar404QuandoRestauranteNaoExiste() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Pizza Teste");
        dto.setPreco(new BigDecimal("30.00"));
        dto.setCategoria("Pizza");
        dto.setRestauranteId(999L);
        
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}

