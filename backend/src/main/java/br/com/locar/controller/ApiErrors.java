package br.com.locar.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.*;
import org.springframework.dao.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.Map;

@RestControllerAdvice
public class ApiErrors {
	@ExceptionHandler(ResponseStatusException.class)
	ResponseEntity<?> business(ResponseStatusException e) {
		return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", ObjectsReason(e)));
	}

	private String ObjectsReason(ResponseStatusException e) {
		return e.getReason() == null ? "Operação inválida" : e.getReason();
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	ResponseEntity<?> conflict() {
		return ResponseEntity.status(409)
				.body(Map.of("message", "Dados duplicados ou registro vinculado a um histórico"));
	}

	@ExceptionHandler({ OptimisticLockingFailureException.class, PessimisticLockingFailureException.class })
	ResponseEntity<?> concurrent() {
		return ResponseEntity.status(409)
				.body(Map.of("message", "Registro alterado em outra sessão. Atualize a página."));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<?> invalid() {
		return ResponseEntity.badRequest().body(Map.of("message", "Formato dos dados inválido"));
	}
}
