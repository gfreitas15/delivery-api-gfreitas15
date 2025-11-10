package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ApiResponse;
import com.deliverytech.delivery_api.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
@Tag(name = "Relatórios", description = "Operações relacionadas a relatórios e análises")
@RequiredArgsConstructor
public class RelatorioController {
    
    private final RelatorioService service;
    
    @GetMapping("/vendas-por-restaurante")
    @Operation(summary = "Vendas por restaurante", description = "Retorna relatório de vendas agrupadas por restaurante")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> vendasPorRestaurante(
            @Parameter(description = "Data de início (formato: yyyy-MM-ddTHH:mm:ss)", example = "2024-01-01T00:00:00")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @Parameter(description = "Data de fim (formato: yyyy-MM-ddTHH:mm:ss)", example = "2024-01-31T23:59:59")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        List<Map<String, Object>> response = service.vendasPorRestaurante(dataInicio, dataFim);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/produtos-mais-vendidos")
    @Operation(summary = "Produtos mais vendidos", description = "Retorna ranking dos produtos mais vendidos")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> produtosMaisVendidos(
            @Parameter(description = "Limite de resultados (opcional)", example = "10")
            @RequestParam(required = false) Integer limite) {
        List<Map<String, Object>> response = service.produtosMaisVendidos(limite);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/clientes-ativos")
    @Operation(summary = "Clientes mais ativos", description = "Retorna ranking dos clientes mais ativos")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> clientesAtivos(
            @Parameter(description = "Limite de resultados (opcional)", example = "10")
            @RequestParam(required = false) Integer limite) {
        List<Map<String, Object>> response = service.clientesAtivos(limite);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/pedidos-por-periodo")
    @Operation(summary = "Pedidos por período", description = "Retorna lista de pedidos em um período específico")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> pedidosPorPeriodo(
            @Parameter(description = "Data de início (formato: yyyy-MM-ddTHH:mm:ss)", example = "2024-01-01T00:00:00")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @Parameter(description = "Data de fim (formato: yyyy-MM-ddTHH:mm:ss)", example = "2024-01-31T23:59:59")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        List<Map<String, Object>> response = service.pedidosPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

