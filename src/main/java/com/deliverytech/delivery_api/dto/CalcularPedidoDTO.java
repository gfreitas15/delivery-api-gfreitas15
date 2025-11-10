package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para cálculo de pedido sem salvar")
public class CalcularPedidoDTO {
    
    @NotNull(message = "ID do restaurante é obrigatório")
    @Positive(message = "ID do restaurante deve ser positivo")
    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long restauranteId;
    
    @Valid
    @NotNull(message = "Itens são obrigatórios")
    @Schema(description = "Lista de itens do pedido", required = true)
    private List<ItemPedidoDTO> itens;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Item do pedido")
    public static class ItemPedidoDTO {
        @NotNull(message = "ID do produto é obrigatório")
        @Positive(message = "ID do produto deve ser positivo")
        @Schema(description = "ID do produto", example = "1", required = true)
        private Long produtoId;
        
        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser positiva")
        @Schema(description = "Quantidade do produto", example = "2", required = true)
        private Integer quantidade;
    }
}

