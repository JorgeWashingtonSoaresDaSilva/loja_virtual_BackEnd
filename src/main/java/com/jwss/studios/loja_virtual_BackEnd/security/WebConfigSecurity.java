package com.jwss.studios.loja_virtual_BackEnd.security;

import com.jwss.studios.loja_virtual_BackEnd.service.ImplementacaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity {
    @Autowired
    private  ImplementacaoUserDetailsService implementacaoUserDetailsService;



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
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
