package br.com.locar.model;

import jakarta.persistence.*;
import java.util.*;
import java.math.BigDecimal;

@Entity
@Table(name = "clientes")
public class Cliente {
	@Id
	public Long id;
	@Column(nullable = false)
	public String nome;
	@Column(nullable = false)
	public String rg;
	@Column(nullable = false, unique = true)
	public String cpf;
	@Column(nullable = false)
	public String endereco;
	@Column(nullable = false)
	public String profissao;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "empregos", joinColumns = @JoinColumn(name = "cliente_id"))
	@OrderColumn(name = "ordem")
	public List<Emprego> empregos = new ArrayList<>();

	@Embeddable
	public static class Emprego {
		public String empregador;
		@Column(precision = 12, scale = 2)
		public BigDecimal rendimento;
	}
}
