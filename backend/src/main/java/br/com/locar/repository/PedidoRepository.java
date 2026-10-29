package br.com.locar.repository;

import br.com.locar.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import java.util.*;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	List<Pedido> findByClienteIdOrderByIdDesc(Long id);

	List<Pedido> findAllByOrderByIdDesc();

	boolean existsByClienteId(Long id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from Pedido p where p.id=:id")
	Optional<Pedido> lockById(@Param("id") Long id);
}
