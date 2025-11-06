package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	private final RestauranteRepository restauranteRepository;
	private final ModelMapper modelMapper;

	public ProdutoService(ProdutoRepository produtoRepository, RestauranteRepository restauranteRepository, ModelMapper modelMapper) {
		this.produtoRepository = produtoRepository;
		this.restauranteRepository = restauranteRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional
	public ProdutoResponseDTO cadastrarProduto(ProdutoDTO dto) {
		Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
			.orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + dto.getRestauranteId()));
		
		if (!restaurante.isAtivo()) {
			throw new BusinessException("Restaurante não está disponível");
		}
		
		Produto produto = modelMapper.map(dto, Produto.class);
		produto.setRestaurante(restaurante);
		produto.setDisponivel(true);
		
		Produto salvo = produtoRepository.save(produto);
		ProdutoResponseDTO response = modelMapper.map(salvo, ProdutoResponseDTO.class);
		response.setRestauranteNome(restaurante.getNome());
		return response;
	}

	public List<ProdutoResponseDTO> buscarProdutosPorRestaurante(Long restauranteId) {
		return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId).stream()
			.map(produto -> {
				ProdutoResponseDTO response = modelMapper.map(produto, ProdutoResponseDTO.class);
				response.setRestauranteNome(produto.getRestaurante().getNome());
				return response;
			})
			.collect(Collectors.toList());
	}

	public ProdutoResponseDTO buscarProdutoPorId(Long id) {
		Produto produto = produtoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
		
		if (!produto.isDisponivel()) {
			throw new BusinessException("Produto não está disponível");
		}
		
		ProdutoResponseDTO response = modelMapper.map(produto, ProdutoResponseDTO.class);
		response.setRestauranteNome(produto.getRestaurante().getNome());
		return response;
	}

	@Transactional
	public ProdutoResponseDTO atualizarProduto(Long id, ProdutoDTO dto) {
		Produto existente = produtoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
		
		Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
			.orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + dto.getRestauranteId()));
		
		existente.setNome(dto.getNome());
		existente.setPreco(dto.getPreco());
		existente.setCategoria(dto.getCategoria());
		existente.setRestaurante(restaurante);
		
		Produto atualizado = produtoRepository.save(existente);
		ProdutoResponseDTO response = modelMapper.map(atualizado, ProdutoResponseDTO.class);
		response.setRestauranteNome(restaurante.getNome());
		return response;
	}

	@Transactional
	public void alterarDisponibilidade(Long id, boolean disponivel) {
		Produto existente = produtoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
		existente.setDisponivel(disponivel);
		produtoRepository.save(existente);
	}

	public List<ProdutoResponseDTO> buscarProdutosPorCategoria(String categoria) {
		return produtoRepository.findByCategoriaAndDisponivelTrue(categoria).stream()
			.map(produto -> {
				ProdutoResponseDTO response = modelMapper.map(produto, ProdutoResponseDTO.class);
				response.setRestauranteNome(produto.getRestaurante().getNome());
				return response;
			})
			.collect(Collectors.toList());
	}

	public List<ProdutoResponseDTO> listarTodos() {
		return produtoRepository.findAll().stream()
			.map(produto -> {
				ProdutoResponseDTO response = modelMapper.map(produto, ProdutoResponseDTO.class);
				response.setRestauranteNome(produto.getRestaurante().getNome());
				return response;
			})
			.collect(Collectors.toList());
	}
}


