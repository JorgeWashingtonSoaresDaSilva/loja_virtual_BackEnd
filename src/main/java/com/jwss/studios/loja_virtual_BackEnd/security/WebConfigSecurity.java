package com.jwss.studios.loja_virtual_BackEnd.security;

import com.jwss.studios.loja_virtual_BackEnd.service.ImplementacaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Substitui a antiga @EnableGlobalMethodSecurity
public class WebConfigSecurity {

    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService;

    @Autowired
    private JWTTokenAutenticacaoService tokenAutenticacaoService;

    // No Spring Boot 4, a segurança é configurada através de um @Bean do SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authConfiguration) throws Exception {

        // Obtém o AuthenticationManager sem loops ou travas de inicialização
        AuthenticationManager authManager = authConfiguration.getAuthenticationManager();

        http
                // 1. Desativa a proteção CSRF para APIs REST
                .csrf(csrf -> csrf.disable())

                // 2. Define a política de sessão como STATELESS (Exigência do JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. Regras de autorização usando a nova sintaxe de expressões lambda e requestMatchers
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/index").permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated() // Qualquer outra rota exige token
                )

                // 4. Configuração moderna do fluxo de Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index")
                        .invalidateHttpSession(true)
                );


        // 5. Inserção na ordem correta para o Spring Boot 4

        // Segundo: O filtro de API roda depois, validando os tokens das demais rotas
        http.addFilterAfter(new JwtApiAuthenticacaoFilter(tokenAutenticacaoService), UsernamePasswordAuthenticationFilter.class);

        // Segundo: Deixamos o filtro de login interceptar a rota /login
        http.addFilterBefore(new JWTLoginFilter("/login", authManager, tokenAutenticacaoService), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    // Bean necessário para gerenciar o processo de autenticação e criptografia de senhas
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(implementacaoUserDetailsService)
                .passwordEncoder(passwordEncoder()); // <-- GARANTA QUE ESSA LINHA ESTÁ AQUI

        return authenticationManagerBuilder.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
