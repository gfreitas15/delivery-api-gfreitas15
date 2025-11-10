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
@Schema(description = "DTO de resposta para restaurante")
public class RestauranteResponseDTO {
    
    @Schema(description = "ID do restaurante", example = "1")
    private Long id;
    
    @Schema(description = "Nome do restaurante", example = "Pizza Express")
    private String nome;
    
    @Schema(description = "Categoria do restaurante", example = "Pizza")
    private String categoria;
    
    @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123")
    private String endereco;
    
    @Schema(description = "CEP do restaurante", example = "01310-100")
    private String cep;
    
    @Schema(description = "Taxa de entrega", example = "5.00")
    private BigDecimal taxaEntrega;
    
    @Schema(description = "Tempo estimado de entrega", example = "30-45 min")
    private String tempoEntrega;
    
    @Schema(description = "Avaliação do restaurante", example = "4.5")
    private BigDecimal avaliacao;
    
    @Schema(description = "Status ativo", example = "true")
    private Boolean ativo;
    
    @Schema(description = "Data de cadastro")
    private LocalDateTime dataCadastro;
}

