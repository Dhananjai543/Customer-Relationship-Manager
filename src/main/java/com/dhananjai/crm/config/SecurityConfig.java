package com.dhananjai.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(configurer -> configurer
						.requestMatchers("/css/**", "/js/**").permitAll() //initially non authenticated users does not have access to css, so this is added
						.requestMatchers("/**").permitAll()); // allow unauthenticated access to sign up page
		return http.build();
	}
	
}
