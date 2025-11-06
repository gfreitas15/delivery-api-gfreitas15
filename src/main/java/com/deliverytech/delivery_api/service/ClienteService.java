package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;

	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Transactional
	public Cliente criar(Cliente cliente) {
		if (clienteRepository.existsByEmail(cliente.getEmail())) {
			throw new IllegalArgumentException("E-mail já cadastrado");
		}
		cliente.setAtivo(true);
		return clienteRepository.save(cliente);
	}

	public List<Cliente> listarTodos() {
		return clienteRepository.findAll();
	}

	public List<Cliente> listarAtivos() {
		return clienteRepository.findByAtivoTrue();
	}

	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
	}

	@Transactional
	public Cliente atualizar(Long id, Cliente dados) {
		Cliente existente = buscarPorId(id);
		if (!existente.getEmail().equals(dados.getEmail()) && clienteRepository.existsByEmail(dados.getEmail())) {
			throw new IllegalArgumentException("E-mail já cadastrado");
		}
		existente.setNome(dados.getNome());
		existente.setEmail(dados.getEmail());
		return clienteRepository.save(existente);
	}

	@Transactional
	public void inativar(Long id) {
		Cliente existente = buscarPorId(id);
		existente.setAtivo(false);
		clienteRepository.save(existente);
	}
}


