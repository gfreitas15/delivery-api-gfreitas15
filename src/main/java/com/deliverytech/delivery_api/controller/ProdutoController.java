package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
public class ProdutoController {

	private final ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@PostMapping("/restaurantes/{restauranteId}/produtos")
	public ResponseEntity<Produto> criar(@PathVariable Long restauranteId, @RequestBody Produto produto) {
		Produto salvo = produtoService.criar(restauranteId, produto);
		return ResponseEntity.created(URI.create("/produtos/" + salvo.getId())).body(salvo);
	}

	@GetMapping("/restaurantes/{restauranteId}/produtos")
	public List<Produto> listarPorRestaurante(@PathVariable Long restauranteId,
	                                         @RequestParam(value = "disponivel", required = false) Boolean disponivel) {
		if (Boolean.TRUE.equals(disponivel)) return produtoService.listarDisponiveisPorRestaurante(restauranteId);
		return produtoService.listarPorRestaurante(restauranteId);
	}

	@GetMapping("/produtos")
	public List<Produto> listarPorCategoria(@RequestParam(value = "categoria", required = false) String categoria,
	                                       @RequestParam(value = "disponivel", required = false) Boolean disponivel) {
		if (categoria != null && Boolean.TRUE.equals(disponivel)) return produtoService.listarPorCategoria(categoria); // simples
		if (categoria != null) return produtoService.listarPorCategoria(categoria);
		return produtoService.listarPorCategoria("");
	}

	@PutMapping("/produtos/{id}")
	public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
		return produtoService.atualizar(id, produto);
	}

	@DeleteMapping("/produtos/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		produtoService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}


