package br.com.locar.repository;

import br.com.locar.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	boolean existsByCpfAndIdNot(String cpf, Long id);
}
