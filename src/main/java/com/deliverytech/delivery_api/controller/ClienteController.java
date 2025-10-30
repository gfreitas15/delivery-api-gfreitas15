package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private final ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@PostMapping
	public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
		Cliente salvo = clienteService.criar(cliente);
		return ResponseEntity.created(URI.create("/clientes/" + salvo.getId())).body(salvo);
	}

	@GetMapping
	public List<Cliente> listar() {
		return clienteService.listarTodos();
	}

	@GetMapping("/ativos")
	public List<Cliente> listarAtivos() {
		return clienteService.listarAtivos();
	}

	@GetMapping("/{id}")
	public Cliente buscar(@PathVariable Long id) {
		return clienteService.buscarPorId(id);
	}

	@PutMapping("/{id}")
	public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
		return clienteService.atualizar(id, cliente);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> inativar(@PathVariable Long id) {
		clienteService.inativar(id);
		return ResponseEntity.noContent().build();
	}
}


