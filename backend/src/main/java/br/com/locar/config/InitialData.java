package br.com.locar.config;

import br.com.locar.model.*;
import br.com.locar.repository.*;
import br.com.locar.service.LocacaoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import tools.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class InitialData {
	@Bean
	CommandLineRunner seed(UsuarioRepository users, AutomovelRepository cars, LocacaoService service,
			PasswordEncoder pw, Environment env) {
		return args -> {
			boolean demo = env.getProperty("app.demo", Boolean.class, false);
			if (demo) {
				if (users.findByUsername("admin").isEmpty()) {
					Usuario admin = new Usuario();
					admin.username = "admin";
					admin.nome = "Administrador";
					admin.papel = Usuario.Papel.ADMIN;
					admin.senha = pw.encode("admin");
					users.save(admin);
				}
				agent(users, pw, "empresa", "Empresa Demo", Usuario.Papel.EMPRESA, "Demo@12345");
				agent(users, pw, "banco", "Banco Demo", Usuario.Papel.BANCO, "Demo@12345");
				if (users.findByUsername("cliente").isEmpty()) {
					Cliente c = new Cliente();
					c.nome = "Cliente Demonstração";
					c.cpf = "00000000000";
					c.rg = "DEMO-001";
					c.endereco = "Rua de demonstração, 100 — Belo Horizonte";
					c.profissao = "Analista";
					service.register(new LocacaoService.Cadastro("cliente", "Demo@12345", c));
				}
			}
			String bank = env.getProperty("AGENT_BANK_PASSWORD");
			String company = env.getProperty("AGENT_COMPANY_PASSWORD");
			if (bank != null)
				agent(users, pw, "banco", env.getProperty("AGENT_BANK_NAME", "Banco parceiro"), Usuario.Papel.BANCO,
						bank);
			if (company != null)
				agent(users, pw, "empresa", env.getProperty("AGENT_COMPANY_NAME", "Locadora parceira"),
						Usuario.Papel.EMPRESA, company);
			String adminPassword = env.getProperty("ADMIN_PASSWORD");
			if (!demo && adminPassword != null)
				agent(users, pw, "admin", "Administrador", Usuario.Papel.ADMIN, adminPassword);
			if (demo || env.getProperty("CATALOG_SEED", Boolean.class, false)) {
				try (var source = new ClassPathResource("catalogo.json").getInputStream()) {
					Automovel[] catalog = new ObjectMapper().readValue(source, Automovel[].class);
					for (Automovel a : catalog) {
						if (cars.findByMatricula(a.matricula).isPresent())
							continue;
						a.proprietarioTipo = Usuario.Papel.EMPRESA;
						a.proprietarioNome = "Locar";
						cars.save(a);
					}
				}
			}
		};
	}

	private void agent(UsuarioRepository users, PasswordEncoder pw, String username, String name, Usuario.Papel role,
			String password) {
		if (users.findByUsername(username).isPresent())
			return;
		if (password.length() < 8 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72)
			throw new IllegalArgumentException("Senha de agente: mínimo 8 caracteres e máximo 72 bytes");
		Usuario u = new Usuario();
		u.username = username;
		u.nome = name;
		u.papel = role;
		u.senha = pw.encode(password);
		users.save(u);
	}
}
