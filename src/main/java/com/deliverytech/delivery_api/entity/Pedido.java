package com.deliverytech.delivery_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;

	@Column(nullable = false)
	private String status; // PENDENTE, EM_PREPARO, SAIU_ENTREGA, ENTREGUE, CANCELADO

	@Column(name = "data_pedido", nullable = false)
	private LocalDate dataPedido;

	@Column(name = "valor_subtotal", nullable = false)
	private BigDecimal valorSubtotal = BigDecimal.ZERO;

	@Column(name = "valor_entrega", nullable = false)
	private BigDecimal valorEntrega = BigDecimal.ZERO;

	@Column(name = "valor_total", nullable = false)
	private BigDecimal valorTotal = BigDecimal.ZERO;

	public Pedido() {}

	@PrePersist
	public void prePersist() {
		if (dataPedido == null) {
			dataPedido = LocalDate.now();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDate dataPedido) {
		this.dataPedido = dataPedido;
	}

	public BigDecimal getValorSubtotal() {
		return valorSubtotal;
	}

	public void setValorSubtotal(BigDecimal valorSubtotal) {
		this.valorSubtotal = valorSubtotal;
	}

	public BigDecimal getValorEntrega() {
		return valorEntrega;
	}

	public void setValorEntrega(BigDecimal valorEntrega) {
		this.valorEntrega = valorEntrega;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
}


