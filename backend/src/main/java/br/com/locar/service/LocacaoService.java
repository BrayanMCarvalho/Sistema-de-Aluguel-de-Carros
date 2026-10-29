package br.com.locar.service;

import br.com.locar.model.*;
import br.com.locar.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import static br.com.locar.model.Pedido.Status.*;
import static br.com.locar.model.Usuario.Papel.*;

@Service
@Transactional
public class LocacaoService {
	private final UsuarioRepository users;
	private final ClienteRepository clients;
	private final AutomovelRepository cars;
	private final PedidoRepository orders;
	private final ContratoRepository contracts;
	private final PasswordEncoder passwords;

	public LocacaoService(UsuarioRepository u, ClienteRepository c, AutomovelRepository a, PedidoRepository p,
			ContratoRepository co, PasswordEncoder pw) {
		users = u;
		clients = c;
		cars = a;
		orders = p;
		contracts = co;
		passwords = pw;
	}

	public Usuario user(String name) {
		return users.findByUsername(name).orElseThrow(() -> error(401, "Sessão inválida"));
	}

	public static ResponseStatusException error(int code, String message) {
		return new ResponseStatusException(HttpStatus.valueOf(code), message);
	}

	public static void require(boolean ok, String msg) {
		if (!ok)
			throw error(400, msg);
	}

	public static String text(String value, String label) {
		require(value != null && !value.isBlank() && value.length() <= 255,
				label + " é obrigatório (máximo 255 caracteres)");
		return value.trim();
	}

	public record Cadastro(String username, String senha, Cliente cliente) {
	}

	public Usuario register(Cadastro input) {
		String name = text(input.username(), "Usuário").toLowerCase(Locale.ROOT);
		require(name.matches("[a-z0-9._@-]{3,100}"), "Usuário deve ter 3 a 100 caracteres sem espaços");
		require(input.senha() != null && input.senha().length() >= 8
				&& input.senha().getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72,
				"Senha deve ter pelo menos 8 caracteres e até 72 bytes");
		if (users.findByUsername(name).isPresent())
			throw error(409, "Usuário já cadastrado");
		Cliente c = input.cliente();
		validateClient(c, 0L);
		Usuario u = new Usuario();
		u.username = name;
		u.senha = passwords.encode(input.senha());
		u.nome = c.nome;
		u.papel = CLIENTE;
		users.saveAndFlush(u);
		c.id = u.id;
		clients.save(c);
		return u;
	}

	void validateClient(Cliente c, Long id) {
		require(c != null, "Informe os dados do cliente");
		c.nome = text(c.nome, "Nome");
		c.rg = text(c.rg, "RG");
		c.cpf = text(c.cpf, "CPF").replaceAll("\\D", "");
		require(c.cpf.length() == 11, "CPF deve conter 11 dígitos");
		if (clients.existsByCpfAndIdNot(c.cpf, id))
			throw error(409, "CPF já cadastrado");
		c.endereco = text(c.endereco, "Endereço");
		c.profissao = text(c.profissao, "Profissão");
		require(c.empregos != null && c.empregos.size() <= 3, "Informe no máximo três empregos");
		for (var e : c.empregos) {
			require(e != null, "Emprego inválido");
			e.empregador = text(e.empregador, "Empregador");
			require(e.rendimento != null && e.rendimento.signum() >= 0
					&& e.rendimento.compareTo(new BigDecimal("9999999999.99")) <= 0 && e.rendimento.scale() <= 2,
					"Rendimento inválido");
		}
	}

	public List<Cliente> clients(Usuario u) {
		return u.papel == CLIENTE ? List.of(client(u, u.id)) : clients.findAll();
	}

	public Cliente client(Usuario u, Long id) {
		if (u.papel == CLIENTE && !u.id.equals(id))
			throw error(403, "Acesso restrito ao próprio cadastro");
		return clients.findById(id).orElseThrow(() -> error(404, "Cliente não encontrado"));
	}

	public Cliente updateClient(Usuario u, Long id, Cliente input) {
		client(u, id);
		validateClient(input, id);
		input.id = id;
		Usuario owner = users.findById(id).orElseThrow();
		owner.nome = input.nome;
		users.save(owner);
		return clients.save(input);
	}

	public void deleteClient(Usuario u, Long id) {
		client(u, id);
		if (orders.existsByClienteId(id))
			throw error(409, "Cadastro com histórico de pedidos não pode ser excluído");
		clients.deleteById(id);
		clients.flush();
		users.deleteById(id);
	}

	public List<Automovel> cars() {
		return cars.findAll();
	}

	public List<Usuario> banks() {
		return users.findAll().stream().filter(u -> u.papel == BANCO).toList();
	}

	public List<Pedido> orders(Usuario u) {
		return u.papel == CLIENTE ? orders.findByClienteIdOrderByIdDesc(u.id) : orders.findAllByOrderByIdDesc();
	}

	public Pedido order(Usuario u, Long id) {
		Pedido p = orders.lockById(id).orElseThrow(() -> error(404, "Pedido não encontrado"));
		if (u.papel == CLIENTE && !p.clienteId.equals(u.id))
			throw error(403, "Acesso restrito ao próprio pedido");
		return p;
	}

	public record PedidoInput(Long automovelId, LocalDate inicio, LocalDate fim, String observacoes) {
	}

	public Pedido create(Usuario u, PedidoInput input) {
		if (u.papel != CLIENTE)
			throw error(403, "Somente clientes criam pedidos");
		client(u, u.id);
		Pedido p = new Pedido();
		p.clienteId = u.id;
		fill(p, input);
		return orders.save(p);
	}

	void fill(Pedido p, PedidoInput i) {
		require(i.inicio() != null && !i.inicio().isBefore(LocalDate.now()), "Início deve ser hoje ou uma data futura");
		require(i.fim() != null && i.fim().isAfter(i.inicio()), "Devolução deve ser posterior à retirada");
		require(ChronoUnit.DAYS.between(i.inicio(), i.fim()) <= 365, "Prazo máximo: 365 dias");
		require(i.automovelId() != null, "Selecione um automóvel");
		p.automovel = cars.findById(i.automovelId()).orElseThrow(() -> error(404, "Automóvel não encontrado"));
		p.inicio = i.inicio();
		p.fim = i.fim();
		require(i.observacoes() == null || i.observacoes().length() <= 255, "Observações: máximo 255 caracteres");
		p.observacoes = i.observacoes();
		p.total = p.automovel.diaria.multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(p.inicio, p.fim)));
		p.status = EM_ANALISE;
		p.parecer = null;
		p.agenteId = null;
	}

	public Pedido update(Usuario u, Long id, PedidoInput i) {
		Pedido p = order(u, id);
		require(p.status == EM_ANALISE || p.status == APROVADO || p.status == REPROVADO,
				"Pedido encerrado não pode ser alterado");
		fill(p, i);
		return orders.save(p);
	}

	public Pedido cancel(Usuario u, Long id) {
		Pedido p = order(u, id);
		require(p.status != CONTRATADO && p.status != CANCELADO, "Pedido encerrado não pode ser cancelado");
		p.status = CANCELADO;
		return orders.save(p);
	}

	public record Avaliacao(boolean aprovado, String parecer) {
	}

	public Pedido evaluate(Usuario u, Long id, Avaliacao i) {
		if (u.papel == CLIENTE)
			throw error(403, "Somente agentes avaliam pedidos");
		Pedido p = order(u, id);
		require(p.status == EM_ANALISE, "Pedido não está em análise");
		p.parecer = text(i.parecer(), "Parecer");
		p.agenteId = u.id;
		p.status = i.aprovado() ? APROVADO : REPROVADO;
		return orders.save(p);
	}

	public record ContratoInput(Usuario.Papel proprietarioTipo, String proprietarioNome, BigDecimal valorCredito,
			Integer parcelas) {
	}

	public Contrato contract(Usuario u, Long id, ContratoInput i) {
		if (u.papel == CLIENTE)
			throw error(403, "Somente agentes emitem contratos");
		Pedido p = order(u, id);
		require(p.status == APROVADO, "O pedido precisa estar aprovado");
		require(!p.inicio.isBefore(LocalDate.now()), "Pedido com retirada passada deve ser atualizado e reavaliado");
		// Serializa a emissão por automóvel para impedir contratos com períodos
		// sobrepostos.
		Automovel car = cars.findById(p.automovel.id).orElseThrow();
		entityManager.lock(car, jakarta.persistence.LockModeType.PESSIMISTIC_WRITE);
		require(orders.findAll().stream().noneMatch(o -> o.status == CONTRATADO && o.automovel.id.equals(car.id)
				&& o.inicio.isBefore(p.fim) && p.inicio.isBefore(o.fim)), "Automóvel já contratado neste período");
		require(i.proprietarioTipo() != null, "Informe o tipo do proprietário");
		require(i.proprietarioTipo() != ADMIN, "Administrador não é um tipo de proprietário");
		Contrato c = new Contrato();
		c.pedidoId = p.id;
		c.clienteId = p.clienteId;
		c.proprietarioTipo = i.proprietarioTipo();
		c.proprietarioNome = text(i.proprietarioNome(), "Proprietário");
		if (i.valorCredito() != null) {
			require(u.papel == BANCO, "Crédito deve ser concedido por um agente bancário autenticado");
			require(i.valorCredito().signum() > 0 && i.valorCredito().compareTo(p.total) <= 0
					&& i.valorCredito().scale() <= 2, "Crédito deve ser positivo e não superar o total");
			require(i.parcelas() != null && i.parcelas() > 0 && i.parcelas() <= 120, "Parcelas: de 1 a 120");
			c.bancoId = u.id;
			c.valorCredito = i.valorCredito();
			c.parcelas = i.parcelas();
		} else
			require(i.parcelas() == null, "Parcelas exigem contrato de crédito");
		car.proprietarioTipo = c.proprietarioTipo;
		car.proprietarioNome = c.proprietarioNome;
		cars.save(car);
		p.status = CONTRATADO;
		orders.save(p);
		return contracts.save(c);
	}

	@jakarta.persistence.PersistenceContext
	private jakarta.persistence.EntityManager entityManager;

	public List<Contrato> contracts(Usuario u) {
		return u.papel == CLIENTE ? contracts.findByClienteIdOrderByIdDesc(u.id) : contracts.findAll();
	}

	public List<Usuario> users(Usuario u) {
		admin(u);
		return users.findAll();
	}

	void admin(Usuario u) {
		if (u.papel != ADMIN)
			throw error(403, "Acesso exclusivo do administrador");
	}

	public record AgenteInput(String username, String senha, String nome, Usuario.Papel papel) {
	}

	public Usuario createAgent(Usuario u, AgenteInput i) {
		admin(u);
		require(i.papel() == BANCO || i.papel() == EMPRESA, "Selecione empresa ou banco");
		String name = text(i.username(), "Usuário").toLowerCase(Locale.ROOT);
		require(name.matches("[a-z0-9._@-]{3,100}"), "Usuário inválido");
		require(i.senha() != null && i.senha().length() >= 8
				&& i.senha().getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72,
				"Senha: mínimo 8 caracteres, máximo 72 bytes");
		if (users.findByUsername(name).isPresent())
			throw error(409, "Usuário já cadastrado");
		Usuario a = new Usuario();
		a.username = name;
		a.nome = text(i.nome(), "Nome");
		a.papel = i.papel();
		a.senha = passwords.encode(i.senha());
		return users.save(a);
	}
}
