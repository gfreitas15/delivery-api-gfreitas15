package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ApiResponse;
import com.deliverytech.delivery_api.dto.PagedResponse;
import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@Tag(name = "Restaurantes", description = "Operações relacionadas aos restaurantes")
@RequiredArgsConstructor
public class RestauranteController {
    
    private final RestauranteService service;
    
    @PostMapping
    @Operation(summary = "Cadastrar restaurante", description = "Cria um novo restaurante no sistema")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso",
            content = @Content(schema = @Schema(implementation = RestauranteResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Restaurante já existe")
    })
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> cadastrar(
            @Valid @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do restaurante a ser criado",
                required = true
            ) RestauranteDTO dto) {
        RestauranteResponseDTO response = service.cadastrar(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create("/api/restaurantes/" + response.getId()))
            .body(ApiResponse.success(response, "Restaurante criado com sucesso"));
    }
    
    @GetMapping
    @Operation(summary = "Listar restaurantes", description = "Lista restaurantes com filtros opcionais e paginação")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    public ResponseEntity<PagedResponse<RestauranteResponseDTO>> listar(
            @Parameter(description = "Categoria do restaurante", example = "Pizza")
            @RequestParam(required = false) String categoria,
            @Parameter(description = "Status ativo do restaurante", example = "true")
            @RequestParam(required = false) Boolean ativo,
            @Parameter(description = "Número da página (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "nome")
            @RequestParam(defaultValue = "nome") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RestauranteResponseDTO> restaurantes = service.listar(categoria, ativo, pageable);
        
        PagedResponse.PageInfo pageInfo = new PagedResponse.PageInfo(
            restaurantes.getNumber(),
            restaurantes.getSize(),
            restaurantes.getTotalElements(),
            restaurantes.getTotalPages()
        );
        
        String baseUrl = "/api/restaurantes?categoria=" + (categoria != null ? categoria : "") +
                         "&ativo=" + (ativo != null ? ativo : "") +
                         "&page=";
        
        PagedResponse.NavigationLinks links = new PagedResponse.NavigationLinks(
            baseUrl + "0&size=" + size,
            baseUrl + (restaurantes.getTotalPages() - 1) + "&size=" + size,
            restaurantes.hasNext() ? baseUrl + (page + 1) + "&size=" + size : null,
            restaurantes.hasPrevious() ? baseUrl + (page - 1) + "&size=" + size : null
        );
        
        PagedResponse<RestauranteResponseDTO> response = new PagedResponse<>(
            restaurantes.getContent(),
            pageInfo,
            links
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna um restaurante específico pelo ID")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurante encontrado",
            content = @Content(schema = @Schema(implementation = RestauranteResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> buscarPorId(
            @Parameter(description = "ID do restaurante", example = "1", required = true)
            @PathVariable Long id) {
        RestauranteResponseDTO response = service.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = RestauranteResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> atualizar(
            @Parameter(description = "ID do restaurante", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RestauranteDTO dto) {
        RestauranteResponseDTO response = service.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Restaurante atualizado com sucesso"));
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do restaurante", description = "Ativa ou desativa um restaurante")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Status atualizado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<Void> atualizarStatus(
            @Parameter(description = "ID do restaurante", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Status ativo", example = "true", required = true)
            @RequestParam Boolean ativo) {
        service.atualizarStatus(id, ativo);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar restaurantes por categoria", description = "Retorna todos os restaurantes de uma categoria específica")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> buscarPorCategoria(
            @Parameter(description = "Categoria do restaurante", example = "Pizza", required = true)
            @PathVariable String categoria) {
        List<RestauranteResponseDTO> response = service.buscarPorCategoria(categoria);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/{id}/taxa-entrega/{cep}")
    @Operation(summary = "Calcular taxa de entrega", description = "Calcula a taxa de entrega para um CEP específico")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Taxa calculada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<BigDecimal>> calcularTaxaEntrega(
            @Parameter(description = "ID do restaurante", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "CEP para cálculo", example = "01310-100", required = true)
            @PathVariable String cep) {
        BigDecimal taxa = service.calcularTaxaEntrega(id, cep);
        return ResponseEntity.ok(ApiResponse.success(taxa));
    }
    
    @GetMapping("/proximos/{cep}")
    @Operation(summary = "Buscar restaurantes próximos", description = "Retorna restaurantes próximos a um CEP específico")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    public ResponseEntity<ApiResponse<List<RestauranteResponseDTO>>> buscarProximos(
            @Parameter(description = "CEP para busca", example = "01310-100", required = true)
            @PathVariable String cep) {
        List<RestauranteResponseDTO> response = service.buscarProximos(cep);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

