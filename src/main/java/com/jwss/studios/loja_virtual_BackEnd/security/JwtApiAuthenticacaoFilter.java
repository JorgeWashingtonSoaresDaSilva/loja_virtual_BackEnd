package com.jwss.studios.loja_virtual_BackEnd.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtApiAuthenticacaoFilter extends OncePerRequestFilter {

    private final JWTTokenAutenticacaoService tokenAutenticacaoService;

    // Construtor recebendo o serviço que você configurou no WebConfigSecurity
    public JwtApiAuthenticacaoFilter(JWTTokenAutenticacaoService tokenAutenticacaoService) {
        this.tokenAutenticacaoService = tokenAutenticacaoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. IGNORA A ROTA DE LOGIN: Permite que a requisição de login passe sem exigir token anterior
        /*if ("/login".equals(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }*/

        try {
            // 2. Extrai o token do cabeçalho "Authorization", valida no banco e retorna o usuário com as Roles
            Authentication authentication = tokenAutenticacaoService.getAuthentication(request, response);

            // 3. Se o token for válido e o usuário possuir acessos, injeta o contexto no Spring Security 7
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            // Garante que o contexto seja limpo caso o token esteja corrompido ou expirado
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Continua o fluxo normal da requisição para chegar até o Controller correspondente
        filterChain.doFilter(request, response);
    }
}
