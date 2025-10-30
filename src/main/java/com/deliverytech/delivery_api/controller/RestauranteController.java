package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	private final RestauranteService restauranteService;

	public RestauranteController(RestauranteService restauranteService) {
		this.restauranteService = restauranteService;
	}

	@PostMapping
	public ResponseEntity<Restaurante> criar(@RequestBody Restaurante restaurante) {
		Restaurante salvo = restauranteService.criar(restaurante);
		return ResponseEntity.created(URI.create("/restaurantes/" + salvo.getId())).body(salvo);
	}

	@GetMapping
	public List<Restaurante> listar(@RequestParam(value = "categoria", required = false) String categoria,
	                               @RequestParam(value = "ativos", required = false) Boolean ativos) {
		if (categoria != null) return restauranteService.buscarPorCategoria(categoria);
		if (Boolean.TRUE.equals(ativos)) return restauranteService.listarAtivos();
		return restauranteService.listarTodos();
	}

	@GetMapping("/top-avaliacao")
	public List<Restaurante> topAvaliacao() {
		return restauranteService.topAvaliacao(10);
	}

	@GetMapping("/{id}")
	public Restaurante buscar(@PathVariable Long id) {
		return restauranteService.buscarPorId(id);
	}

	@PutMapping("/{id}")
	public Restaurante atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
		return restauranteService.atualizar(id, restaurante);
	}

	@PostMapping("/{id}/ativar")
	public ResponseEntity<Void> ativar(@PathVariable Long id) {
		restauranteService.ativar(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/inativar")
	public ResponseEntity<Void> inativar(@PathVariable Long id) {
		restauranteService.inativar(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		restauranteService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}


