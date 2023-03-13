package com.packt.cardatabase;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.packt.cardatabase.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	//@Autowired
	private final UserDetailsServiceImpl userDetailsService;

	//@Autowired
	private final AuthenticationFilter authenticationFilter;

	//@Autowired
	private final AuthEntryPoint exceptionHandler;

	public SecurityConfig(UserDetailsServiceImpl userDetailsService, AuthenticationFilter authenticationFilter, AuthEntryPoint exceptionHandler) {
		this.userDetailsService = userDetailsService;
		this.authenticationFilter = authenticationFilter;
		this.exceptionHandler = exceptionHandler;
	}



	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
				csrf().
				disable().
				cors()

				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()

				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.anyRequest().authenticated()
				.and()

				.exceptionHandling()
				.authenticationEntryPoint(exceptionHandler)
				.and()

				.addFilterBefore(authenticationFilter,
				UsernamePasswordAuthenticationFilter.class);
	}
	/**
	 * La configuration CORS est utilisée pour permettre à une ressource web d'être accessible à partir d'un domaine différent de celui à partir duquel elle a été initialement chargée. Cette fonctionnalité est utilisée pour des raisons de sécurité pour éviter que des scripts malveillants ne puissent accéder aux ressources sensibles sur un site web.
	 *
	 * La méthode "corsConfigurationSource()" définit la configuration CORS en utilisant un objet CorsConfiguration, qui permet de définir les origines autorisées, les méthodes HTTP autorisées et les en-têtes autorisés.
	 *
	 * La méthode "setAllowedOrigins()" définit les domaines autorisés à accéder aux ressources web, ici avec l'option "". La méthode "setAllowedMethods()" définit les méthodes HTTP autorisées (par exemple, GET, POST, etc.), également avec l'option "". La méthode "setAllowedHeaders()" définit les en-têtes HTTP autorisés, encore une fois avec l'option "*". Enfin, la méthode "setAllowCredentials()" indique si les demandes avec des informations d'authentification peuvent être envoyées avec la requête CORS.
	 *
	 * La méthode "applyPermitDefaultValues()" applique les valeurs par défaut de la configuration CORS pour les paramètres non définis, tels que "Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS", "Access-Control-Allow-Headers: x-requested-with, authorization, content-type", etc.
	 *
	 * Enfin, la méthode "registerCorsConfiguration()" enregistre la configuration CORS avec la source de configuration, en spécifiant l'emplacement où la configuration doit être appliquée, ici avec l'option "/**" pour tous les chemins d'accès.
	 */

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(false);
		config.applyPermitDefaultValues();

		source.registerCorsConfiguration("/**", config);
		return source;
	}	

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception  {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	public AuthenticationManager getAuthenticationManager() throws 
	Exception {
		return authenticationManager();
	}
}
