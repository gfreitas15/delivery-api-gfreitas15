package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ClienteDTO;
import com.deliverytech.delivery_api.dto.ClienteResponseDTO;
import com.deliverytech.delivery_api.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

	private final ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@PostMapping
	public ResponseEntity<ClienteResponseDTO> cadastrar(@Valid @RequestBody ClienteDTO dto) {
		ClienteResponseDTO response = clienteService.cadastrarCliente(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.location(URI.create("/api/clientes/" + response.getId()))
			.body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
		ClienteResponseDTO response = clienteService.buscarClientePorId(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<ClienteResponseDTO>> listarAtivos() {
		List<ClienteResponseDTO> response = clienteService.listarClientesAtivos();
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
		ClienteResponseDTO response = clienteService.atualizarCliente(id, dto);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<Void> ativarDesativar(@PathVariable Long id) {
		clienteService.ativarDesativarCliente(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email) {
		ClienteResponseDTO response = clienteService.buscarClientePorEmail(email);
		return ResponseEntity.ok(response);
	}
}


