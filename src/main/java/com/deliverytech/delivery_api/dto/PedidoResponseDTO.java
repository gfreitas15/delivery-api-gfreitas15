package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.model.Pedido.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta para pedido")
public class PedidoResponseDTO {
    
    @Schema(description = "ID do pedido", example = "1")
    private Long id;
    
    @Schema(description = "ID do cliente", example = "1")
    private Long clienteId;
    
    @Schema(description = "Nome do cliente", example = "João Silva")
    private String clienteNome;
    
    @Schema(description = "ID do restaurante", example = "1")
    private Long restauranteId;
    
    @Schema(description = "Nome do restaurante", example = "Pizza Express")
    private String restauranteNome;
    
    @Schema(description = "Endereço de entrega", example = "Rua A, 123")
    private String enderecoEntrega;
    
    @Schema(description = "Status do pedido", example = "PENDENTE")
    private StatusPedido status;
    
    @Schema(description = "Subtotal do pedido", example = "71.80")
    private BigDecimal subtotal;
    
    @Schema(description = "Taxa de entrega", example = "5.00")
    private BigDecimal taxaEntrega;
    
    @Schema(description = "Total do pedido", example = "76.80")
    private BigDecimal total;
    
    @Schema(description = "Itens do pedido")
    private List<ItemPedidoResponseDTO> itens;
    
    @Schema(description = "Data de criação")
    private LocalDateTime dataCriacao;
    
    @Schema(description = "Data de atualização")
    private LocalDateTime dataAtualizacao;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Item do pedido")
    public static class ItemPedidoResponseDTO {
        @Schema(description = "ID do item", example = "1")
        private Long id;
        
        @Schema(description = "ID do produto", example = "1")
        private Long produtoId;
        
        @Schema(description = "Nome do produto", example = "Pizza Margherita")
        private String produtoNome;
        
        @Schema(description = "Quantidade", example = "2")
        private Integer quantidade;
        
        @Schema(description = "Preço unitário", example = "35.90")
        private BigDecimal precoUnitario;
        
        @Schema(description = "Subtotal do item", example = "71.80")
        private BigDecimal subtotal;
    }
}

