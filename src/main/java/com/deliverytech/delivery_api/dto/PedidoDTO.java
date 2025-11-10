package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para criação de pedido")
public class PedidoDTO {
    
    @NotNull(message = "ID do cliente é obrigatório")
    @Positive(message = "ID do cliente deve ser positivo")
    @Schema(description = "ID do cliente", example = "1", required = true)
    private Long clienteId;
    
    @NotNull(message = "ID do restaurante é obrigatório")
    @Positive(message = "ID do restaurante deve ser positivo")
    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long restauranteId;
    
    @NotBlank(message = "Endereço de entrega é obrigatório")
    @Size(min = 5, max = 200, message = "Endereço de entrega deve ter entre 5 e 200 caracteres")
    @Schema(description = "Endereço de entrega", example = "Rua A, 123", required = true)
    private String enderecoEntrega;
    
    @Valid
    @NotNull(message = "Itens são obrigatórios")
    @NotEmpty(message = "Pedido deve conter pelo menos um item")
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
        @Max(value = 100, message = "Quantidade não pode exceder 100 unidades")
        @Schema(description = "Quantidade do produto", example = "2", required = true)
        private Integer quantidade;
    }
}

