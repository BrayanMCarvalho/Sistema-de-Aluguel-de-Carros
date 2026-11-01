package br.com.locar.controller;

import br.com.locar.model.*;
import br.com.locar.service.LocacaoService;
import br.com.locar.service.LocacaoService.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {
	private final LocacaoService service;

	public ApiController(LocacaoService service) {
		this.service = service;
	}

	private Usuario user(Principal p) {
		return service.user(p.getName());
	}

	@GetMapping("/auth/csrf")
	Map<String, String> csrf(CsrfToken token) {
		return Map.of("token", token.getToken(), "headerName", token.getHeaderName());
	}

	@PostMapping("/auth/register")
	@ResponseStatus(org.springframework.http.HttpStatus.CREATED)
	Usuario register(@RequestBody Cadastro i) {
		return service.register(i);
	}

	@GetMapping("/auth/me")
	Usuario me(Principal p) {
		return user(p);
	}

	@GetMapping("/clientes")
	List<Cliente> clients(Principal p) {
		return service.clients(user(p));
	}

	@GetMapping("/clientes/{id}")
	Cliente client(Principal p, @PathVariable Long id) {
		return service.client(user(p), id);
	}

	@PutMapping("/clientes/{id}")
	Cliente updateClient(Principal p, @PathVariable Long id, @RequestBody Cliente i) {
		return service.updateClient(user(p), id, i);
	}

	@DeleteMapping("/clientes/{id}")
	@ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
	void deleteClient(Principal p, @PathVariable Long id) {
		service.deleteClient(user(p), id);
	}

	@GetMapping("/automoveis")
	List<Automovel> cars() {
		return service.cars();
	}

	@GetMapping("/bancos")
	List<Usuario> banks() {
		return service.banks();
	}

	@GetMapping("/pedidos")
	List<Pedido> orders(Principal p) {
		return service.orders(user(p));
	}

	@GetMapping("/pedidos/{id}")
	Pedido order(Principal p, @PathVariable Long id) {
		return service.order(user(p), id);
	}

	@PostMapping("/pedidos")
	@ResponseStatus(org.springframework.http.HttpStatus.CREATED)
	Pedido create(Principal p, @RequestBody PedidoInput i) {
		return service.create(user(p), i);
	}

	@PutMapping("/pedidos/{id}")
	Pedido update(Principal p, @PathVariable Long id, @RequestBody PedidoInput i) {
		return service.update(user(p), id, i);
	}

	@PostMapping("/pedidos/{id}/cancelar")
	Pedido cancel(Principal p, @PathVariable Long id) {
		return service.cancel(user(p), id);
	}

	@PostMapping("/pedidos/{id}/avaliar")
	Pedido evaluate(Principal p, @PathVariable Long id, @RequestBody Avaliacao i) {
		return service.evaluate(user(p), id, i);
	}

	@PostMapping("/pedidos/{id}/contrato")
	@ResponseStatus(org.springframework.http.HttpStatus.CREATED)
	Contrato contract(Principal p, @PathVariable Long id, @RequestBody ContratoInput i) {
		return service.contract(user(p), id, i);
	}

	@GetMapping("/contratos")
	List<Contrato> contracts(Principal p) {
		return service.contracts(user(p));
	}

	@GetMapping("/admin/usuarios")
	List<Usuario> users(Principal p) {
		return service.users(user(p));
	}

	@PostMapping("/admin/agentes")
	@ResponseStatus(org.springframework.http.HttpStatus.CREATED)
	Usuario agent(Principal p, @RequestBody AgenteInput i) {
		return service.createAgent(user(p), i);
	}
}
