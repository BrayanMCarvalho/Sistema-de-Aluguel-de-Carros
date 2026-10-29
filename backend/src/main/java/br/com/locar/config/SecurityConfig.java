package br.com.locar.config;

import br.com.locar.repository.UsuarioRepository;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	PasswordEncoder passwords() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService users(UsuarioRepository users) {
		return name -> users.findByUsername(name)
				.map(u -> User.withUsername(u.username).password(u.senha).roles(u.papel.name()).build())
				.orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));
	}

	@Bean
	SecurityFilterChain security(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						a -> a.requestMatchers("/api/auth/csrf", "/api/auth/register", "/api/auth/login", "/error")
								.permitAll().anyRequest().authenticated())
				.formLogin(f -> f.loginProcessingUrl("/api/auth/login")
						.successHandler((req, res, auth) -> {
							res.setContentType("application/json");
							res.getWriter().write("{}");
						})
						.failureHandler((req, res, e) -> res.sendError(401)))
				.logout(l -> l.logoutUrl("/api/auth/logout")
						.logoutSuccessHandler((req, res, auth) -> res.setStatus(204)))
				.exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> res.sendError(401)))
				.build();
	}
}
