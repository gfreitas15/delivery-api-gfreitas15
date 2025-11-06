package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.*;
import com.deliverytech.delivery_api.entity.*;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

	private final PedidoRepository pedidoRepository;
	private final ClienteRepository clienteRepository;
	private final RestauranteRepository restauranteRepository;
	private final ProdutoRepository produtoRepository;
	private final ItemPedidoRepository itemPedidoRepository;
	private final ModelMapper modelMapper;

	public PedidoService(
			PedidoRepository pedidoRepository,
			ClienteRepository clienteRepository,
			RestauranteRepository restauranteRepository,
			ProdutoRepository produtoRepository,
			ItemPedidoRepository itemPedidoRepository,
			ModelMapper modelMapper) {
		this.pedidoRepository = pedidoRepository;
		this.clienteRepository = clienteRepository;
		this.restauranteRepository = restauranteRepository;
		this.produtoRepository = produtoRepository;
		this.itemPedidoRepository = itemPedidoRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional
	public PedidoResponseDTO criarPedido(PedidoDTO dto) {
		Cliente cliente = clienteRepository.findById(dto.getClienteId())
			.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));
		
		if (!cliente.isAtivo()) {
			throw new BusinessException("Cliente inativo não pode criar pedido");
		}
		
		Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
			.orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + dto.getRestauranteId()));
		
		if (!restaurante.isAtivo()) {
			throw new BusinessException("Restaurante não está disponível");
		}
		
		BigDecimal valorSubtotal = BigDecimal.ZERO;
		
		for (ItemPedidoDTO itemDTO : dto.getItens()) {
			Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
				.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));
			
			if (!produto.isDisponivel()) {
				throw new BusinessException("Produto " + produto.getNome() + " não está disponível");
			}
			
			if (!produto.getRestaurante().getId().equals(restaurante.getId())) {
				throw new BusinessException("Produto " + produto.getNome() + " não pertence ao restaurante selecionado");
			}
			
			BigDecimal itemSubtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
			valorSubtotal = valorSubtotal.add(itemSubtotal);
		}
		
		BigDecimal valorEntrega = restaurante.getTaxaEntrega();
		BigDecimal valorTotal = valorSubtotal.add(valorEntrega);
		
		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setStatus("PENDENTE");
		pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
		pedido.setValorSubtotal(valorSubtotal);
		pedido.setValorEntrega(valorEntrega);
		pedido.setValorTotal(valorTotal);
		pedido.setDataPedido(LocalDateTime.now());
		
		Pedido pedidoSalvo = pedidoRepository.save(pedido);
		
		for (ItemPedidoDTO itemDTO : dto.getItens()) {
			Produto produto = produtoRepository.findById(itemDTO.getProdutoId()).orElseThrow();
			ItemPedido itemPedido = new ItemPedido(pedidoSalvo, produto, itemDTO.getQuantidade());
			itemPedidoRepository.save(itemPedido);
		}
		
		return buscarPedidoPorId(pedidoSalvo.getId());
	}

	public PedidoResponseDTO buscarPedidoPorId(Long id) {
		Pedido pedido = pedidoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
		
		PedidoResponseDTO response = modelMapper.map(pedido, PedidoResponseDTO.class);
		response.setClienteId(pedido.getCliente().getId());
		response.setClienteNome(pedido.getCliente().getNome());
		
		List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(id);
		List<ItemPedidoResponseDTO> itensResponse = itens.stream()
			.map(item -> {
				ItemPedidoResponseDTO itemResponse = modelMapper.map(item, ItemPedidoResponseDTO.class);
				itemResponse.setProdutoId(item.getProduto().getId());
				itemResponse.setProdutoNome(item.getProduto().getNome());
				return itemResponse;
			})
			.collect(Collectors.toList());
		
		response.setItens(itensResponse);
		
		if (!itens.isEmpty()) {
			Produto primeiroProduto = itens.get(0).getProduto();
			response.setRestauranteId(primeiroProduto.getRestaurante().getId());
			response.setRestauranteNome(primeiroProduto.getRestaurante().getNome());
		}
		
		return response;
	}

	public List<PedidoResumoDTO> buscarPedidosPorCliente(Long clienteId) {
		clienteRepository.findById(clienteId)
			.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + clienteId));
		
		return pedidoRepository.findByClienteId(clienteId).stream()
			.map(pedido -> {
				PedidoResumoDTO resumo = modelMapper.map(pedido, PedidoResumoDTO.class);
				resumo.setClienteNome(pedido.getCliente().getNome());
				
				List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(pedido.getId());
				if (!itens.isEmpty()) {
					Produto primeiroProduto = itens.get(0).getProduto();
					resumo.setRestauranteNome(primeiroProduto.getRestaurante().getNome());
				}
				
				return resumo;
			})
			.collect(Collectors.toList());
	}

	@Transactional
	public PedidoResponseDTO atualizarStatusPedido(Long id, String status) {
		Pedido pedido = pedidoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
		
		String statusAtual = pedido.getStatus();
		
		if (statusAtual.equals("CANCELADO")) {
			throw new BusinessException("Pedido cancelado não pode ter status alterado");
		}
		
		if (statusAtual.equals("ENTREGUE")) {
			throw new BusinessException("Pedido entregue não pode ter status alterado");
		}
		
		List<String> statusValidos = List.of("PENDENTE", "EM_PREPARO", "SAIU_ENTREGA", "ENTREGUE", "CANCELADO");
		if (!statusValidos.contains(status)) {
			throw new BusinessException("Status inválido: " + status);
		}
		
		pedido.setStatus(status);
		Pedido atualizado = pedidoRepository.save(pedido);
		
		return buscarPedidoPorId(atualizado.getId());
	}

	public BigDecimal calcularTotalPedido(List<ItemPedidoDTO> itens) {
		BigDecimal total = BigDecimal.ZERO;
		
		for (ItemPedidoDTO itemDTO : itens) {
			Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
				.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));
			
			if (!produto.isDisponivel()) {
				throw new BusinessException("Produto " + produto.getNome() + " não está disponível");
			}
			
			BigDecimal itemSubtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
			total = total.add(itemSubtotal);
		}
		
		return total;
	}

	@Transactional
	public void cancelarPedido(Long id) {
		Pedido pedido = pedidoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
		
		if (pedido.getStatus().equals("CANCELADO")) {
			throw new BusinessException("Pedido já está cancelado");
		}
		
		if (pedido.getStatus().equals("ENTREGUE")) {
			throw new BusinessException("Pedido entregue não pode ser cancelado");
		}
		
		if (pedido.getStatus().equals("SAIU_ENTREGA")) {
			throw new BusinessException("Pedido em entrega não pode ser cancelado");
		}
		
		pedido.setStatus("CANCELADO");
		pedidoRepository.save(pedido);
	}

	public List<PedidoResumoDTO> listarTodos() {
		return pedidoRepository.findAll().stream()
			.map(pedido -> {
				PedidoResumoDTO resumo = modelMapper.map(pedido, PedidoResumoDTO.class);
				resumo.setClienteNome(pedido.getCliente().getNome());
				
				List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(pedido.getId());
				if (!itens.isEmpty()) {
					Produto primeiroProduto = itens.get(0).getProduto();
					resumo.setRestauranteNome(primeiroProduto.getRestaurante().getNome());
				}
				
				return resumo;
			})
			.collect(Collectors.toList());
	}
}
