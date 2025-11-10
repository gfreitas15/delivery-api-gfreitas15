package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.validation.ValidCategoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para cadastro/atualização de produto")
public class ProdutoDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    private String nome;
    
    @Size(min = 10, max = 500, message = "Descrição deve ter entre 10 e 500 caracteres")
    @Schema(description = "Descrição do produto", example = "Pizza com molho de tomate, mussarela e manjericão")
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    @DecimalMax(value = "500.00", message = "Preço não pode exceder R$ 500,00")
    @Schema(description = "Preço do produto", example = "35.90", required = true)
    private BigDecimal preco;
    
    @NotBlank(message = "Categoria é obrigatória")
    @ValidCategoria
    @Schema(description = "Categoria do produto", example = "Pizza", required = true)
    private String categoria;
    
    @NotNull(message = "ID do restaurante é obrigatório")
    @Positive(message = "ID do restaurante deve ser positivo")
    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long restauranteId;
    
    @Schema(description = "Disponibilidade do produto", example = "true")
    private Boolean disponivel = true;
}

