package br.com.locar.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pedidos")
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@Version
	public Long versao;
	@Column(nullable = false)
	public Long clienteId;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "automovel_id")
	public Automovel automovel;
	public LocalDate inicio;
	public LocalDate fim;
	@Enumerated(EnumType.STRING)
	public Status status = Status.EM_ANALISE;
	@Column(precision = 12, scale = 2)
	public BigDecimal total;
	public String observacoes;
	public String parecer;
	public Long agenteId;
	public Instant criadoEm = Instant.now();

	public enum Status {
		EM_ANALISE, APROVADO, REPROVADO, CANCELADO, CONTRATADO
	}
}
