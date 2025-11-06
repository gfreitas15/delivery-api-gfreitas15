package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProdutoController {

	private final ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@PostMapping("/produtos")
	public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoDTO dto) {
		ProdutoResponseDTO response = produtoService.cadastrarProduto(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.location(URI.create("/api/produtos/" + response.getId()))
			.body(response);
	}

	@GetMapping("/produtos/{id}")
	public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
		ProdutoResponseDTO response = produtoService.buscarProdutoPorId(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/restaurantes/{restauranteId}/produtos")
	public ResponseEntity<List<ProdutoResponseDTO>> buscarPorRestaurante(@PathVariable Long restauranteId) {
		List<ProdutoResponseDTO> response = produtoService.buscarProdutosPorRestaurante(restauranteId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/produtos/{id}")
	public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
		ProdutoResponseDTO response = produtoService.atualizarProduto(id, dto);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/produtos/{id}/disponibilidade")
	public ResponseEntity<Void> alterarDisponibilidade(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
		Boolean disponivel = body.get("disponivel");
		if (disponivel == null) {
			return ResponseEntity.badRequest().build();
		}
		produtoService.alterarDisponibilidade(id, disponivel);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/produtos/categoria/{categoria}")
	public ResponseEntity<List<ProdutoResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
		List<ProdutoResponseDTO> response = produtoService.buscarProdutosPorCategoria(categoria);
		return ResponseEntity.ok(response);
	}
}


