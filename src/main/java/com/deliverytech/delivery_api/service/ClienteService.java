package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ClienteDTO;
import com.deliverytech.delivery_api.dto.ClienteResponseDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;
	private final ModelMapper modelMapper;

	public ClienteService(ClienteRepository clienteRepository, ModelMapper modelMapper) {
		this.clienteRepository = clienteRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional
	public ClienteResponseDTO cadastrarCliente(ClienteDTO dto) {
		if (clienteRepository.existsByEmail(dto.getEmail())) {
			throw new BusinessException("Email já cadastrado");
		}
		Cliente cliente = modelMapper.map(dto, Cliente.class);
		cliente.setAtivo(true);
		Cliente salvo = clienteRepository.save(cliente);
		return modelMapper.map(salvo, ClienteResponseDTO.class);
	}

	public ClienteResponseDTO buscarClientePorId(Long id) {
		Cliente cliente = clienteRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
		return modelMapper.map(cliente, ClienteResponseDTO.class);
	}

	public ClienteResponseDTO buscarClientePorEmail(String email) {
		Cliente cliente = clienteRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email: " + email));
		return modelMapper.map(cliente, ClienteResponseDTO.class);
	}

	@Transactional
	public ClienteResponseDTO atualizarCliente(Long id, ClienteDTO dto) {
		Cliente existente = clienteRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
		
		if (!existente.getEmail().equals(dto.getEmail()) && clienteRepository.existsByEmail(dto.getEmail())) {
			throw new BusinessException("Email já cadastrado");
		}
		
		existente.setNome(dto.getNome());
		existente.setEmail(dto.getEmail());
		existente.setTelefone(dto.getTelefone());
		existente.setEndereco(dto.getEndereco());
		
		Cliente atualizado = clienteRepository.save(existente);
		return modelMapper.map(atualizado, ClienteResponseDTO.class);
	}

	@Transactional
	public void ativarDesativarCliente(Long id) {
		Cliente existente = clienteRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
		existente.setAtivo(!existente.isAtivo());
		clienteRepository.save(existente);
	}

	public List<ClienteResponseDTO> listarClientesAtivos() {
		return clienteRepository.findByAtivoTrue().stream()
			.map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
			.collect(Collectors.toList());
	}

	public List<ClienteResponseDTO> listarTodos() {
		return clienteRepository.findAll().stream()
			.map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
			.collect(Collectors.toList());
	}
}


