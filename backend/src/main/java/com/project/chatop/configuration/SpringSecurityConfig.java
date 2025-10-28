package com.project.chatop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.chatop.security.JwtRequestFilter;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@EnableWebSecurity
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SpringSecurityConfig {
	
	private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }
	
    /*
     Configuration des règles de sécurité HTTP.
     - Désactive CSRF, formLogin et httpBasic
     - Permet certaines routes sans authentification
     - Définit le filtrage JWT pour toutes les autres requêtes
    */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		.formLogin(form -> form.disable())
		.httpBasic(basic -> basic.disable())
        .authorizeHttpRequests(auth -> auth
        		.requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
        		.requestMatchers("/images/**").permitAll()
        		.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
        		.anyRequest().authenticated())
        
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	    	
	    return http.build();
	}
	
	/*
	 Bean pour encoder les mots de passe avec BCrypt.
	*/
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
