package br.com.locar.repository;

import br.com.locar.model.Automovel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomovelRepository extends JpaRepository<Automovel, Long> {
	java.util.Optional<Automovel> findByMatricula(String matricula);
}
