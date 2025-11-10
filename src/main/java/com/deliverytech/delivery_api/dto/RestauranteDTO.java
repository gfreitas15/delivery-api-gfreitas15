package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.validation.ValidCEP;
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
@Schema(description = "DTO para cadastro/atualização de restaurante")
public class RestauranteDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome do restaurante", example = "Pizza Express", required = true)
    private String nome;
    
    @NotBlank(message = "Categoria é obrigatória")
    @ValidCategoria
    @Schema(description = "Categoria do restaurante", example = "Pizza", required = true)
    private String categoria;
    
    @NotBlank(message = "Endereço é obrigatório")
    @Size(min = 5, max = 200, message = "Endereço deve ter entre 5 e 200 caracteres")
    @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123", required = true)
    private String endereco;
    
    @ValidCEP
    @Schema(description = "CEP do restaurante", example = "01310-100")
    private String cep;
    
    @NotNull(message = "Taxa de entrega é obrigatória")
    @PositiveOrZero(message = "Taxa de entrega deve ser positiva ou zero")
    @DecimalMax(value = "100.00", message = "Taxa de entrega não pode exceder R$ 100,00")
    @Schema(description = "Taxa de entrega", example = "5.00", required = true)
    private BigDecimal taxaEntrega;
    
    @Pattern(regexp = "^\\d{1,3}-\\d{1,3}\\s*min$", message = "Tempo de entrega deve estar no formato: 30-45 min")
    @Schema(description = "Tempo estimado de entrega", example = "30-45 min")
    private String tempoEntrega;
    
    @DecimalMin(value = "0.0", message = "Avaliação deve ser no mínimo 0.0")
    @DecimalMax(value = "5.0", message = "Avaliação deve ser no máximo 5.0")
    @Schema(description = "Avaliação do restaurante (0-5)", example = "4.5")
    private BigDecimal avaliacao;
    
    @Schema(description = "Status ativo do restaurante", example = "true")
    private Boolean ativo = true;
}

