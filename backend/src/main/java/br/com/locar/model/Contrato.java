package br.com.locar.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name = "contratos")
public class Contrato {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@Column(nullable = false, unique = true)
	public Long pedidoId;
	public Long clienteId;
	@Enumerated(EnumType.STRING)
	public Usuario.Papel proprietarioTipo;
	public String proprietarioNome;
	public Long bancoId;
	@Column(precision = 12, scale = 2)
	public BigDecimal valorCredito;
	public Integer parcelas;
	public Instant emitidoEm = Instant.now();
}
