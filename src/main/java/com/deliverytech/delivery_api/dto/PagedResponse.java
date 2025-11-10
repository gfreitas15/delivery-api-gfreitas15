package com.deliverytech.delivery_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta paginada da API")
public class PagedResponse<T> {
    
    @Schema(description = "Conteúdo da página")
    private List<T> content;
    
    @Schema(description = "Informações de paginação")
    private PageInfo page;
    
    @Schema(description = "Links de navegação")
    private NavigationLinks links;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Informações de paginação")
    public static class PageInfo {
        @Schema(description = "Número da página (0-indexed)", example = "0")
        private Integer number;
        
        @Schema(description = "Tamanho da página", example = "10")
        private Integer size;
        
        @Schema(description = "Total de elementos", example = "50")
        private Long totalElements;
        
        @Schema(description = "Total de páginas", example = "5")
        private Integer totalPages;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Links de navegação")
    public static class NavigationLinks {
        @Schema(description = "Link para primeira página")
        private String first;
        
        @Schema(description = "Link para última página")
        private String last;
        
        @Schema(description = "Link para próxima página")
        private String next;
        
        @Schema(description = "Link para página anterior")
        private String prev;
    }
}

