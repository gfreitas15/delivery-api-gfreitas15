package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.*;
import com.deliverytech.delivery_api.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@PostMapping("/pedidos")
	public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoDTO dto) {
		PedidoResponseDTO response = pedidoService.criarPedido(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.location(URI.create("/api/pedidos/" + response.getId()))
			.body(response);
	}

	@GetMapping("/pedidos/{id}")
	public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
		PedidoResponseDTO response = pedidoService.buscarPedidoPorId(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/clientes/{clienteId}/pedidos")
	public ResponseEntity<List<PedidoResumoDTO>> buscarPorCliente(@PathVariable Long clienteId) {
		List<PedidoResumoDTO> response = pedidoService.buscarPedidosPorCliente(clienteId);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/pedidos/{id}/status")
	public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
		String status = body.get("status");
		if (status == null) {
			return ResponseEntity.badRequest().build();
		}
		PedidoResponseDTO response = pedidoService.atualizarStatusPedido(id, status);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/pedidos/{id}")
	public ResponseEntity<Void> cancelar(@PathVariable Long id) {
		pedidoService.cancelarPedido(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/pedidos/calcular")
	public ResponseEntity<Map<String, BigDecimal>> calcularTotal(@Valid @RequestBody List<ItemPedidoDTO> itens) {
		BigDecimal total = pedidoService.calcularTotalPedido(itens);
		return ResponseEntity.ok(Map.of("total", total));
	}
}


