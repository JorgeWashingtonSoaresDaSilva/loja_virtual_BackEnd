package com.jwss.studios.loja_virtual_BackEnd.security;


import com.jwss.studios.loja_virtual_BackEnd.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher; // Mudamos para a interface base
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final JWTTokenAutenticacaoService tokenAutenticacaoService;

    /* Configurando o gerenciador de autenticação */
    public JWTLoginFilter(String url, AuthenticationManager authenticationManager, JWTTokenAutenticacaoService tokenAutenticacaoService) {
        /* Criamos um validador de URL direto na interface base, sem depender do AntPathRequestMatcher */
        super(new RequestMatcher() {
            @Override
            public boolean matches(HttpServletRequest request) {
                return request.getServletPath().equals(url) && "POST".equalsIgnoreCase(request.getMethod());
            }
        });

        this.tokenAutenticacaoService = tokenAutenticacaoService;
        setAuthenticationManager(authenticationManager);
    }

    /* Retorna o usuário ao processar a autenticação */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        /* Obtém o usuário a partir do JSON do Postman */
        Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        /* Retorna o user com login e senha capturados explicitamente dos métodos da entidade */
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha())
        );
    }

    // 1. MÉTODO EXECUTADO QUANDO O LOGIN E SENHA ESTÃO CORRETOS
    @Override
    protected void successfulAuthentication(jakarta.servlet.http.HttpServletRequest request,
                                            jakarta.servlet.http.HttpServletResponse response,
                                            jakarta.servlet.FilterChain chain,
                                            org.springframework.security.core.Authentication authResult)
            throws java.io.IOException, jakarta.servlet.ServletException {
        try {
            // Salva o contexto de autenticação no Spring Security 7
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authResult);

            // Garante o status 200 OK antes de entregar o token
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);

            // Gera o Token JWT e injeta no Header/Body da resposta
            tokenAutenticacaoService.addAuthentication(response, authResult.getName());

            // CORTA O FLUXO: Impede que o Spring Security limpe a resposta ou jogue para telas de erro
            return;

        } catch (Exception e) {
            System.out.println("Erro no successfulAuthentication: " + e.getMessage());
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // 2. MÉTODO EXECUTADO QUANDO O LOGIN OU SENHA ESTÃO ERRADOS
    @Override
    protected void unsuccessfulAuthentication(jakarta.servlet.http.HttpServletRequest request,
                                              jakarta.servlet.http.HttpServletResponse response,
                                              org.springframework.security.core.AuthenticationException failed)
            throws java.io.IOException, jakarta.servlet.ServletException {

        response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String mensagemErro = "Usuário ou senha inválidos.";

        if (failed instanceof org.springframework.security.authentication.BadCredentialsException) {
            mensagemErro = "Senha incorreta! Verifique os dados digitados.";
        } else if (failed instanceof org.springframework.security.core.userdetails.UsernameNotFoundException) {
            mensagemErro = "O login informado não foi encontrado no sistema.";
        } else if (failed instanceof org.springframework.security.authentication.DisabledException) {
            mensagemErro = "Este usuário está desativado no sistema.";
        }

        String jsonResposta = String.format(
                "{\n  \"status\": 401,\n  \"error\": \"Unauthorized\",\n  \"message\": \"%s\"\n}",
                mensagemErro
        );

        java.io.PrintWriter writer = response.getWriter();
        writer.write(jsonResposta);
        writer.flush();
        writer.close();
    }


}
