package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.service.PedidoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@PostMapping("/pedidos")
	public ResponseEntity<Pedido> criar(@RequestParam Long clienteId, @RequestBody Pedido pedido) {
		Pedido salvo = pedidoService.criar(clienteId, pedido);
		return ResponseEntity.created(URI.create("/pedidos/" + salvo.getId())).body(salvo);
	}

	@GetMapping("/pedidos/{id}")
	public Pedido buscar(@PathVariable Long id) {
		return pedidoService.buscarPorId(id);
	}

	@GetMapping("/clientes/{clienteId}/pedidos")
	public List<Pedido> listarPorCliente(@PathVariable Long clienteId) {
		return pedidoService.listarPorCliente(clienteId);
	}

    @GetMapping("/pedidos")
    public List<Pedido> listar(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "inicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(value = "fim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        if (status != null) return pedidoService.listarPorStatus(status);
        if (inicio != null && fim != null) return pedidoService.listarPorPeriodo(inicio, fim);
        return pedidoService.listarTodos();
    }

	@PutMapping("/pedidos/{id}/status")
	public Pedido atualizarStatus(@PathVariable Long id, @RequestParam String status) {
		return pedidoService.atualizarStatus(id, status);
	}
}


