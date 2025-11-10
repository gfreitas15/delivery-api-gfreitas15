package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ApiResponse;
import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
@RequiredArgsConstructor
public class ProdutoController {
    
    private final ProdutoService service;
    
    @PostMapping
    @Operation(summary = "Cadastrar produto", description = "Cria um novo produto no sistema")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
            content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> cadastrar(
            @Valid @RequestBody ProdutoDTO dto) {
        ProdutoResponseDTO response = service.cadastrar(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create("/api/produtos/" + response.getId()))
            .body(ApiResponse.success(response, "Produto criado com sucesso"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo ID")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Produto encontrado",
            content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> buscarPorId(
            @Parameter(description = "ID do produto", example = "1", required = true)
            @PathVariable Long id) {
        ProdutoResponseDTO response = service.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> atualizar(
            @Parameter(description = "ID do produto", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ProdutoDTO dto) {
        ProdutoResponseDTO response = service.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Produto atualizado com sucesso"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto", description = "Remove um produto do sistema")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID do produto", example = "1", required = true)
            @PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/disponibilidade")
    @Operation(summary = "Alternar disponibilidade do produto", description = "Alterna a disponibilidade de um produto")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Disponibilidade atualizada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> toggleDisponibilidade(
            @Parameter(description = "ID do produto", example = "1", required = true)
            @PathVariable Long id) {
        service.toggleDisponibilidade(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/restaurantes/{restauranteId}/produtos")
    @Operation(summary = "Buscar produtos de um restaurante", description = "Retorna todos os produtos de um restaurante específico")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorRestaurante(
            @Parameter(description = "ID do restaurante", example = "1", required = true)
            @PathVariable Long restauranteId,
            @Parameter(description = "Filtrar por disponibilidade", example = "true")
            @RequestParam(required = false) Boolean disponivel) {
        List<ProdutoResponseDTO> response = service.buscarPorRestaurante(restauranteId, disponivel);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar produtos por categoria", description = "Retorna todos os produtos de uma categoria específica")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorCategoria(
            @Parameter(description = "Categoria do produto", example = "Pizza", required = true)
            @PathVariable String categoria) {
        List<ProdutoResponseDTO> response = service.buscarPorCategoria(categoria);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar produtos por nome", description = "Busca produtos pelo nome (busca parcial)")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarPorNome(
            @Parameter(description = "Nome do produto (busca parcial)", example = "Pizza", required = true)
            @RequestParam String nome) {
        List<ProdutoResponseDTO> response = service.buscarPorNome(nome);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

