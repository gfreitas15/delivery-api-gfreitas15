package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestauranteService {

	private final RestauranteRepository restauranteRepository;
	private final ModelMapper modelMapper;

	public RestauranteService(RestauranteRepository restauranteRepository, ModelMapper modelMapper) {
		this.restauranteRepository = restauranteRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional
	public RestauranteResponseDTO cadastrarRestaurante(RestauranteDTO dto) {
		Restaurante restaurante = modelMapper.map(dto, Restaurante.class);
		restaurante.setAtivo(true);
		Restaurante salvo = restauranteRepository.save(restaurante);
		return modelMapper.map(salvo, RestauranteResponseDTO.class);
	}

	public RestauranteResponseDTO buscarRestaurantePorId(Long id) {
		Restaurante restaurante = restauranteRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + id));
		return modelMapper.map(restaurante, RestauranteResponseDTO.class);
	}

	public List<RestauranteResponseDTO> buscarRestaurantesPorCategoria(String categoria) {
		return restauranteRepository.findByCategoria(categoria).stream()
			.map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
			.collect(Collectors.toList());
	}

	public List<RestauranteResponseDTO> buscarRestaurantesDisponiveis() {
		return restauranteRepository.findByAtivoTrue().stream()
			.map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
			.collect(Collectors.toList());
	}

	@Transactional
	public RestauranteResponseDTO atualizarRestaurante(Long id, RestauranteDTO dto) {
		Restaurante existente = restauranteRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + id));
		
		existente.setNome(dto.getNome());
		existente.setCategoria(dto.getCategoria());
		existente.setTaxaEntrega(dto.getTaxaEntrega());
		if (dto.getAvaliacao() != null) {
			existente.setAvaliacao(dto.getAvaliacao());
		}
		
		Restaurante atualizado = restauranteRepository.save(existente);
		return modelMapper.map(atualizado, RestauranteResponseDTO.class);
	}

	public BigDecimal calcularTaxaEntrega(Long restauranteId, String cep) {
		Restaurante restaurante = restauranteRepository.findById(restauranteId)
			.orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + restauranteId));
		
		if (!restaurante.isAtivo()) {
			throw new IllegalArgumentException("Restaurante não está disponível");
		}
		
		BigDecimal taxaBase = restaurante.getTaxaEntrega();
		
		if (cep == null || cep.isEmpty()) {
			return taxaBase;
		}
		
		String cepSemFormatacao = cep.replaceAll("[^0-9]", "");
		
		if (cepSemFormatacao.length() != 8) {
			return taxaBase;
		}
		
		return taxaBase;
	}

	public List<RestauranteResponseDTO> listarTodos() {
		return restauranteRepository.findAll().stream()
			.map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
			.collect(Collectors.toList());
	}
}


