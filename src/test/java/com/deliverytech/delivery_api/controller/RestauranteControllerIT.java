package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.model.Restaurante;
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
class RestauranteControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private RestauranteRepository repository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Restaurante restaurante;
    
    @BeforeEach
    void setUp() {
        repository.deleteAll();
        restaurante = new Restaurante();
        restaurante.setNome("Pizza Express");
        restaurante.setCategoria("Pizza");
        restaurante.setEndereco("Rua das Flores, 123");
        restaurante.setCep("01310-100");
        restaurante.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante.setTempoEntrega("30-45 min");
        restaurante.setAvaliacao(new BigDecimal("4.5"));
        restaurante.setAtivo(true);
        restaurante = repository.save(restaurante);
    }
    
    @Test
    void deveCadastrarRestaurante() throws Exception {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Burger King");
        dto.setCategoria("Hambúrguer");
        dto.setEndereco("Av. Paulista, 1000");
        dto.setTaxaEntrega(new BigDecimal("3.50"));
        dto.setAtivo(true);
        
        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nome").value("Burger King"));
    }
    
    @Test
    void deveBuscarRestaurantePorId() throws Exception {
        mockMvc.perform(get("/api/restaurantes/{id}", restaurante.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(restaurante.getId()))
                .andExpect(jsonPath("$.data.nome").value("Pizza Express"));
    }
    
    @Test
    void deveRetornar404QuandoRestauranteNaoExiste() throws Exception {
        mockMvc.perform(get("/api/restaurantes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
    
    @Test
    void deveListarRestaurantes() throws Exception {
        mockMvc.perform(get("/api/restaurantes")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page.totalElements").value(1));
    }
    
    @Test
    void deveAtualizarRestaurante() throws Exception {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Pizza Express Atualizado");
        dto.setCategoria("Pizza");
        dto.setEndereco("Rua das Flores, 123");
        dto.setTaxaEntrega(new BigDecimal("6.00"));
        dto.setAtivo(true);
        
        mockMvc.perform(put("/api/restaurantes/{id}", restaurante.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nome").value("Pizza Express Atualizado"));
    }
    
    @Test
    void deveAtualizarStatusRestaurante() throws Exception {
        mockMvc.perform(patch("/api/restaurantes/{id}/status", restaurante.getId())
                .param("ativo", "false"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        RestauranteDTO dto = new RestauranteDTO();
        // DTO sem nome obrigatório
        
        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}

