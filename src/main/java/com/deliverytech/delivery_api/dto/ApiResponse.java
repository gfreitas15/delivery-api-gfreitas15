package com.deliverytech.delivery_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta padronizada da API")
public class ApiResponse<T> {
    
    @Schema(description = "Indica se a operação foi bem-sucedida", example = "true")
    private Boolean success;
    
    @Schema(description = "Dados da resposta")
    private T data;
    
    @Schema(description = "Mensagem descritiva", example = "Operação realizada com sucesso")
    private String message;
    
    @Schema(description = "Timestamp da resposta", example = "2024-01-15T10:30:00")
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "Operação realizada com sucesso", LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message, LocalDateTime.now());
    }
}

