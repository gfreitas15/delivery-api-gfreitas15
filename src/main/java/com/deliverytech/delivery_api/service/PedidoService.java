package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

	private final PedidoRepository pedidoRepository;
	private final ClienteRepository clienteRepository;

	public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
		this.pedidoRepository = pedidoRepository;
		this.clienteRepository = clienteRepository;
	}

	@Transactional
	public Pedido criar(Long clienteId, Pedido pedido) {
		Cliente cliente = clienteRepository.findById(clienteId)
			.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
		if (!cliente.isAtivo()) {
			throw new IllegalStateException("Cliente inativo não pode criar pedido");
		}
		pedido.setCliente(cliente);
		if (pedido.getStatus() == null) {
			pedido.setStatus("PENDENTE");
		}
		BigDecimal subtotal = pedido.getValorSubtotal() == null ? BigDecimal.ZERO : pedido.getValorSubtotal();
		BigDecimal entrega = pedido.getValorEntrega() == null ? BigDecimal.ZERO : pedido.getValorEntrega();
		pedido.setValorSubtotal(subtotal);
		pedido.setValorEntrega(entrega);
		pedido.setValorTotal(subtotal.add(entrega));
		return pedidoRepository.save(pedido);
	}

	public Pedido buscarPorId(Long id) {
		return pedidoRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
	}

	public List<Pedido> listarPorCliente(Long clienteId) {
		return pedidoRepository.findByClienteId(clienteId);
	}

	public List<Pedido> listarPorStatus(String status) {
		return pedidoRepository.findByStatus(status);
	}

	public List<Pedido> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
		return pedidoRepository.findByDataPedidoBetween(inicio, fim);
	}

	@Transactional
	public Pedido atualizarStatus(Long id, String novoStatus) {
		Pedido pedido = buscarPorId(id);
		pedido.setStatus(novoStatus);
		return pedidoRepository.save(pedido);
	}
}


