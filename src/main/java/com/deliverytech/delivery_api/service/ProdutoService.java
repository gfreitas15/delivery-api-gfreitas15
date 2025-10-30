package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	private final RestauranteRepository restauranteRepository;

	public ProdutoService(ProdutoRepository produtoRepository, RestauranteRepository restauranteRepository) {
		this.produtoRepository = produtoRepository;
		this.restauranteRepository = restauranteRepository;
	}

	@Transactional
	public Produto criar(Long restauranteId, Produto produto) {
		if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Preço deve ser positivo");
		}
		Restaurante restaurante = restauranteRepository.findById(restauranteId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
		produto.setRestaurante(restaurante);
		produto.setDisponivel(true);
		return produtoRepository.save(produto);
	}

	public List<Produto> listarPorRestaurante(Long restauranteId) {
		return produtoRepository.findByRestauranteId(restauranteId);
	}

	public List<Produto> listarDisponiveisPorRestaurante(Long restauranteId) {
		return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId);
	}

	public List<Produto> listarPorCategoria(String categoria) {
		return produtoRepository.findByCategoria(categoria);
	}

	public List<Produto> listarDisponiveis() {
		return produtoRepository.findByDisponivelTrue();
	}

	public List<Produto> listarDisponiveisPorCategoria(String categoria) {
		return produtoRepository.findByCategoriaAndDisponivelTrue(categoria);
	}

	public List<Produto> listarTodos() {
		return produtoRepository.findAll();
	}

	@Transactional
	public Produto atualizar(Long id, Produto dados) {
		Produto existente = produtoRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
		if (dados.getPreco() != null && dados.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Preço deve ser positivo");
		}
		existente.setNome(dados.getNome());
		existente.setPreco(dados.getPreco());
		existente.setCategoria(dados.getCategoria());
		existente.setDisponivel(dados.isDisponivel());
		return produtoRepository.save(existente);
	}

	@Transactional
	public void deletar(Long id) {
		Produto existente = produtoRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
		produtoRepository.delete(existente);
	}
}


