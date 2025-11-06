package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

public interface RelatorioVendasDTO {
    
    Long getRestauranteId();
    
    String getNomeRestaurante();
    
    BigDecimal getTotalVendas();
}

