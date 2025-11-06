package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {

	private final RestauranteService restauranteService;

	public RestauranteController(RestauranteService restauranteService) {
		this.restauranteService = restauranteService;
	}

	@PostMapping
	public ResponseEntity<RestauranteResponseDTO> cadastrar(@Valid @RequestBody RestauranteDTO dto) {
		RestauranteResponseDTO response = restauranteService.cadastrarRestaurante(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.location(URI.create("/api/restaurantes/" + response.getId()))
			.body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
		RestauranteResponseDTO response = restauranteService.buscarRestaurantePorId(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<RestauranteResponseDTO>> listarDisponiveis() {
		List<RestauranteResponseDTO> response = restauranteService.buscarRestaurantesDisponiveis();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/categoria/{categoria}")
	public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
		List<RestauranteResponseDTO> response = restauranteService.buscarRestaurantesPorCategoria(categoria);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteDTO dto) {
		RestauranteResponseDTO response = restauranteService.atualizarRestaurante(id, dto);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}/taxa-entrega/{cep}")
	public ResponseEntity<BigDecimal> calcularTaxaEntrega(@PathVariable Long id, @PathVariable String cep) {
		BigDecimal taxa = restauranteService.calcularTaxaEntrega(id, cep);
		return ResponseEntity.ok(taxa);
	}
}


