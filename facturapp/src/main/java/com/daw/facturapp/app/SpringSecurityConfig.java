package com.daw.facturapp.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.daw.facturapp.app.models.services.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	// Rutas de recursos.
	String[] resources = new String[] {
			"/css/**", "/js/**", "/images/**", "/locale"
	};
	
	/**
	 * Codificador de contraseñas.
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}
	
	/**
	 * Configuarición del control de acceso a las rutas
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			// Permitimos a todos el acceso a las páginas de recursos.
			.antMatchers(resources).permitAll()
			// Cualquiera puede acceder a la página inicio y al registro.
			.antMatchers("/", "/index", "/registry").permitAll()
			.antMatchers("/confirm-account").permitAll()
			.antMatchers("/reset/*").permitAll()
			.antMatchers("/admin", "/admin/*").access("hasRole('ADMIN')")
				// Todas las demás páginas, requieren autenticacion.
				.anyRequest().authenticated()
				.and()
			// Formulario de logueo.
			.formLogin()
				.loginPage("/login").permitAll() 	// Página de login.
				.defaultSuccessUrl("/")					// Redirección en caso de login exitoso.
				.failureUrl("/login?error=true")		// Redirección en caso de login no exitoso
				.usernameParameter("username")			// Name del input para el usuario.
				.passwordParameter("password")			// Name del input para la contraseña.
				.and()
			.logout().permitAll()
				.logoutSuccessUrl("/login?logout");
	}
	
	//@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder authBuilder) 
			throws Exception {
		// Autenticación con JPA.
		// Aquí se especifica el login y el encriptador de la contraseña.
		authBuilder.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}

}
