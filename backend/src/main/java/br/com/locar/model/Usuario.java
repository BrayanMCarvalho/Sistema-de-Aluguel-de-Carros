package br.com.locar.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@Column(nullable = false, unique = true)
	public String username;
	@JsonIgnore
	@Column(nullable = false)
	public String senha;
	@Column(nullable = false)
	public String nome;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public Papel papel;

	public enum Papel {
		CLIENTE, EMPRESA, BANCO, ADMIN
	}
}
