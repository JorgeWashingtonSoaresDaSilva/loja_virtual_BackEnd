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

        String requestURI = request.getRequestURI();

        // SE FOR A ROTA DE CADASTRO, IGNORA A VALIDAÇÃO DO FILTRO JWT E SEGUE O FLUXO PÚBLICO
        if (requestURI.contains("/salvarPj")) {
            filterChain.doFilter(request, response);
            return; // O fluxo morre aqui para rotas públicas, não atingindo o try/catch
        }

        try {
            // 2. Extrai o token do cabeçalho "Authorization", valida no banco e retorna o usuário com as Roles
            Authentication authentication = tokenAutenticacaoService.getAuthentication(request, response);

            // 3. Se o token for válido e o usuário possuir acessos, injeta o contexto no Spring Security
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // CORREÇÃO: O fluxo normal para rotas seguras deve continuar estritamente AQUI
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Garante que o contexto seja limpo caso o token esteja corrompido ou expirado
            SecurityContextHolder.clearContext();
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // Escreve a mensagem amigável para o cliente
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"Ocorreu um erro no sistema avise o administrador:\"}" + "\n" + e.getMessage());
            response.getWriter().flush();
        }
    }
}
