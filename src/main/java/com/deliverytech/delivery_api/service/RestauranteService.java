package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestauranteService {

	private final RestauranteRepository restauranteRepository;

	public RestauranteService(RestauranteRepository restauranteRepository) {
		this.restauranteRepository = restauranteRepository;
	}

	@Transactional
	public Restaurante criar(Restaurante restaurante) {
		restaurante.setAtivo(true);
		return restauranteRepository.save(restaurante);
	}

	public List<Restaurante> listarTodos() {
		return restauranteRepository.findAll();
	}

	public Restaurante buscarPorId(Long id) {
		return restauranteRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
	}

	@Transactional
	public Restaurante atualizar(Long id, Restaurante dados) {
		Restaurante existente = buscarPorId(id);
		existente.setNome(dados.getNome());
		existente.setCategoria(dados.getCategoria());
		existente.setAvaliacao(dados.getAvaliacao());
		existente.setTaxaEntrega(dados.getTaxaEntrega());
		return restauranteRepository.save(existente);
	}

	@Transactional
	public void ativar(Long id) {
		Restaurante existente = buscarPorId(id);
		existente.setAtivo(true);
		restauranteRepository.save(existente);
	}

	@Transactional
	public void inativar(Long id) {
		Restaurante existente = buscarPorId(id);
		existente.setAtivo(false);
		restauranteRepository.save(existente);
	}

	public List<Restaurante> buscarPorCategoria(String categoria) {
		return restauranteRepository.findByCategoria(categoria);
	}

	public List<Restaurante> listarAtivos() {
		return restauranteRepository.findByAtivoTrue();
	}

	public List<Restaurante> topAvaliacao(int limit) {
		List<Restaurante> todos = restauranteRepository.findAll();
		return todos.stream()
			.sorted((a, b) -> {
				if (a.getAvaliacao() == null && b.getAvaliacao() == null) return 0;
				if (a.getAvaliacao() == null) return 1;
				if (b.getAvaliacao() == null) return -1;
				return b.getAvaliacao().compareTo(a.getAvaliacao());
			})
			.limit(limit > 0 ? limit : todos.size())
			.toList();
	}

	@Transactional
	public void deletar(Long id) {
		Restaurante existente = buscarPorId(id);
		restauranteRepository.delete(existente);
	}
}


