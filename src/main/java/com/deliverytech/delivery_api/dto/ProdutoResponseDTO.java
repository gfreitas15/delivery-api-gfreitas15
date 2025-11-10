package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta para produto")
public class ProdutoResponseDTO {
    
    @Schema(description = "ID do produto", example = "1")
    private Long id;
    
    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    private String nome;
    
    @Schema(description = "Descrição do produto")
    private String descricao;
    
    @Schema(description = "Preço do produto", example = "35.90")
    private BigDecimal preco;
    
    @Schema(description = "Categoria do produto", example = "Pizza")
    private String categoria;
    
    @Schema(description = "Disponibilidade", example = "true")
    private Boolean disponivel;
    
    @Schema(description = "ID do restaurante", example = "1")
    private Long restauranteId;
    
    @Schema(description = "Nome do restaurante", example = "Pizza Express")
    private String restauranteNome;
    
    @Schema(description = "Data de cadastro")
    private LocalDateTime dataCadastro;
}

