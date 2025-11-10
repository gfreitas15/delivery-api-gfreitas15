package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ApiResponse;
import com.deliverytech.delivery_api.dto.CalcularPedidoDTO;
import com.deliverytech.delivery_api.dto.CalcularPedidoResponseDTO;
import com.deliverytech.delivery_api.dto.PagedResponse;
import com.deliverytech.delivery_api.dto.PedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoResponseDTO;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.service.PedidoService;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operações relacionadas aos pedidos")
@RequiredArgsConstructor
public class PedidoController {
    
    private final PedidoService service;
    
    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido no sistema")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente, restaurante ou produto não encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Erro de negócio (restaurante inativo, produto indisponível, etc)")
    })
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> criar(
            @Valid @RequestBody PedidoDTO dto) {
        PedidoResponseDTO response = service.criar(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create("/api/pedidos/" + response.getId()))
            .body(ApiResponse.success(response, "Pedido criado com sucesso"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo ID com todos os detalhes")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pedido encontrado",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> buscarPorId(
            @Parameter(description = "ID do pedido", example = "1", required = true)
            @PathVariable Long id) {
        PedidoResponseDTO response = service.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Lista pedidos com filtros opcionais e paginação")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    })
    public ResponseEntity<PagedResponse<PedidoResponseDTO>> listar(
            @Parameter(description = "Status do pedido", example = "PENDENTE")
            @RequestParam(required = false) Pedido.StatusPedido status,
            @Parameter(description = "Número da página (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação", example = "dataCriacao")
            @RequestParam(defaultValue = "dataCriacao") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PedidoResponseDTO> pedidos = service.listar(status, pageable);
        
        PagedResponse.PageInfo pageInfo = new PagedResponse.PageInfo(
            pedidos.getNumber(),
            pedidos.getSize(),
            pedidos.getTotalElements(),
            pedidos.getTotalPages()
        );
        
        String baseUrl = "/api/pedidos?status=" + (status != null ? status : "") + "&page=";
        
        PagedResponse.NavigationLinks links = new PagedResponse.NavigationLinks(
            baseUrl + "0&size=" + size,
            baseUrl + (pedidos.getTotalPages() - 1) + "&size=" + size,
            pedidos.hasNext() ? baseUrl + (page + 1) + "&size=" + size : null,
            pedidos.hasPrevious() ? baseUrl + (page - 1) + "&size=" + size : null
        );
        
        PagedResponse<PedidoResponseDTO> response = new PagedResponse<>(
            pedidos.getContent(),
            pageInfo,
            links
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Status não pode ser alterado")
    })
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> atualizarStatus(
            @Parameter(description = "ID do pedido", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Novo status do pedido", example = "CONFIRMADO", required = true)
            @RequestParam Pedido.StatusPedido status) {
        PedidoResponseDTO response = service.atualizarStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(response, "Status atualizado com sucesso"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido existente")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Pedido não pode ser cancelado")
    })
    public ResponseEntity<Void> cancelar(
            @Parameter(description = "ID do pedido", example = "1", required = true)
            @PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/clientes/{clienteId}/pedidos")
    @Operation(summary = "Histórico de pedidos do cliente", description = "Retorna todos os pedidos de um cliente específico")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    })
    public ResponseEntity<ApiResponse<List<PedidoResponseDTO>>> buscarPorCliente(
            @Parameter(description = "ID do cliente", example = "1", required = true)
            @PathVariable Long clienteId) {
        List<PedidoResponseDTO> response = service.buscarPorCliente(clienteId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/restaurantes/{restauranteId}/pedidos")
    @Operation(summary = "Pedidos do restaurante", description = "Retorna todos os pedidos de um restaurante específico")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    })
    public ResponseEntity<ApiResponse<List<PedidoResponseDTO>>> buscarPorRestaurante(
            @Parameter(description = "ID do restaurante", example = "1", required = true)
            @PathVariable Long restauranteId) {
        List<PedidoResponseDTO> response = service.buscarPorRestaurante(restauranteId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @PostMapping("/calcular")
    @Operation(summary = "Calcular total do pedido", description = "Calcula o total de um pedido sem salvá-lo no sistema")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Total calculado com sucesso",
            content = @Content(schema = @Schema(implementation = CalcularPedidoResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante ou produto não encontrado")
    })
    public ResponseEntity<ApiResponse<CalcularPedidoResponseDTO>> calcular(
            @Valid @RequestBody CalcularPedidoDTO dto) {
        CalcularPedidoResponseDTO response = service.calcular(dto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

