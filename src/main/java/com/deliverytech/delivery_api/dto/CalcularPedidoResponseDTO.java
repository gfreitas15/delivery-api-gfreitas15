package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta para cálculo de pedido")
public class CalcularPedidoResponseDTO {
    
    @Schema(description = "Subtotal do pedido", example = "71.80")
    private BigDecimal subtotal;
    
    @Schema(description = "Taxa de entrega", example = "5.00")
    private BigDecimal taxaEntrega;
    
    @Schema(description = "Total do pedido", example = "76.80")
    private BigDecimal total;
}

