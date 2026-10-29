package br.com.locar.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "automoveis")
public class Automovel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@Column(nullable = false, unique = true)
	public String matricula;
	public int ano;
	public String marca;
	public String modelo;
	@Column(unique = true)
	public String placa;
	@Column(precision = 12, scale = 2)
	public BigDecimal diaria;
	@Enumerated(EnumType.STRING)
	public Usuario.Papel proprietarioTipo;
	public String proprietarioNome;
	@Column(length = 2000)
	public String imagemUrl;
	@Column(length = 2000)
	public String imagemFonte;
	@Column(length = 1000)
	public String imagemCredito;
	public String categoria;
}
