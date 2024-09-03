package com.dhananjai.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dhananjai.crm.controller.CustomAccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityFilter {


    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;


	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> 
                	exceptionHandling.accessDeniedHandler(customAccessDeniedHandler))
                .authorizeHttpRequests( authConfig -> {
                	authConfig.requestMatchers("/resources/**","/").permitAll();
//                    authConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
//                    authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
                    authConfig.requestMatchers("/error").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/customer/delete").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET, "/customer/**").hasAnyRole("USER","ADMIN");
                    authConfig.requestMatchers("/**").permitAll();
                    authConfig.anyRequest().authenticated();

                });

        return http.build();

    }
}