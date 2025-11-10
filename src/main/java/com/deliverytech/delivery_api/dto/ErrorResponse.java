package com.deliverytech.delivery_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Resposta de erro padronizada seguindo RFC 7807 (Problem Details for HTTP APIs)
 * https://tools.ietf.org/html/rfc7807
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta de erro padronizada (RFC 7807)")
public class ErrorResponse {
    
    @Schema(description = "Timestamp do erro", example = "2024-01-15T10:30:00")
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @Schema(description = "Código HTTP do erro", example = "400")
    private Integer status;
    
    @Schema(description = "Tipo do erro", example = "Bad Request")
    private String error;
    
    @Schema(description = "Mensagem principal do erro", example = "Dados inválidos")
    private String message;
    
    @Schema(description = "Caminho do endpoint que gerou o erro", example = "/api/restaurantes")
    private String path;
    
    @Schema(description = "Detalhes específicos do erro (para validações)")
    private Map<String, String> details;
    
    @Schema(description = "Lista de erros de validação")
    private List<ValidationError> validationErrors;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Erro de validação")
    public static class ValidationError {
        @Schema(description = "Campo com erro", example = "email")
        private String field;
        
        @Schema(description = "Mensagem de erro", example = "Email inválido")
        private String message;
        
        @Schema(description = "Valor rejeitado", example = "email@")
        private Object rejectedValue;
    }
}

