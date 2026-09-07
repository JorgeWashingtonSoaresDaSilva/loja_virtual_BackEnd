package com.jwss.studios.loja_virtual_BackEnd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa proteção contra CSRF (desnecessária em APIs com Token)

                // CONFIGURAÇÃO STATELESS (Substitui completamente o uso de HttpSessions)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        // 🟢 POST liberado para todos
                        .requestMatchers(HttpMethod.POST, "/salvarAcesso").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/deletarAcesso").permitAll()

                        // ❌ GET bloqueado explicitamente para todos (retorna 403 Forbidden)
                          .requestMatchers(HttpMethod.GET, "/salvarAcesso").denyAll()

                        // 🔒 Todo o restante do sistema exige autenticação
                        .anyRequest().authenticated()
                );

        return http.build();
    }


}
