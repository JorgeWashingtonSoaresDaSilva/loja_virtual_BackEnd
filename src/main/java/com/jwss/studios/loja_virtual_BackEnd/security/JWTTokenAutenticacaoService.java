package com.jwss.studios.loja_virtual_BackEnd.security;

import com.jwss.studios.loja_virtual_BackEnd.ApplicationContextLoad;
import com.jwss.studios.loja_virtual_BackEnd.model.Usuario;
import com.jwss.studios.loja_virtual_BackEnd.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTTokenAutenticacaoService {

    // Validade de 15 dias (1.296.000.000 ms)
    private static final long EXPIRATION_TIME = 15L * 24 * 60 * 60 * 1000;

    // Garanta que esta chave permaneça segura e secreta em produção (ideal vir do application.properties)
    private static final String SECRET = "minha_palavra_secreta_super_segura_com_mais_de_32_caracteres";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    private SecretKey getChaveCriptografica() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Gera o token e responde ao cliente
    public void addAuthentication(HttpServletResponse response, String username) throws Exception {
        // 1. Gera a String compacta do JWT (Sua sintaxe JJWT moderna que está ótima!)
        String JWT = Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getChaveCriptografica())
                .compact();

        String token = TOKEN_PREFIX + " " + JWT;

        // 2. Adiciona no Header HTTP para o navegador/frontend ler
        response.addHeader(HEADER_STRING, token);
        liberacaoCors(response);

        // 3. O AJUSTE: Garante o tipo de conteúdo e força a escrita no corpo da resposta
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Abre o escritor, escreve o JSON com o token e garante o envio imediato
        java.io.PrintWriter writer = response.getWriter();
        writer.write("{\"Authorization\":\"" + token + "\"}");
        writer.flush(); // Empurra os dados para a rede
        writer.close(); // Fecha o fluxo impedindo que o Spring Security limpe a resposta
    }


    // Valida o token recebido e retorna a autenticação do Spring Security
    // Modificado para o nome padrão 'getAuthentication' para alinhar com o nosso filtro anterior
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            try {
                String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

                // Decodificação fluente do JJWT 0.12+
                Claims claims = Jwts.parser()
                        .verifyWith(getChaveCriptografica())
                        .build()
                        .parseSignedClaims(tokenLimpo)
                        .getPayload();

                String loginUsuario = claims.getSubject();

                if (loginUsuario != null) {
                    // Busca o repositório dinamicamente via Contexto do Spring
                    Usuario usuario = ApplicationContextLoad
                            .getApplicationContext()
                            .getBean(UsuarioRepository.class)
                            .findUserByLogin(loginUsuario);

                    if (usuario != null) {
                        return new UsernamePasswordAuthenticationToken(
                                usuario.getUsername(),
                                usuario.getPassword(),
                                usuario.getAuthorities()
                        );
                    }
                }

                // Certifique-se de adicionar este import no topo do arquivo se quiser capturar apenas a assinatura:
                // import io.jsonwebtoken.security.SignatureException;
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                System.out.println("Token expirado: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // Escreve a mensagem amigável para o cliente
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"Token expirado.\"}");
                response.getWriter().flush();
            } catch (io.jsonwebtoken.security.SignatureException e) { // IMPORTANTE: Caminho completo para evitar erro de import
                try {
                    System.out.println("Token inválido/corrompido: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                    // Escreve a mensagem amigável para o cliente
                   response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"error\": \"Token inválido.\"}");
                    response.getWriter().flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
                // Captura qualquer outro erro genérico do JWT (como token malformado)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                liberacaoCors(response);
            }
        }
        return null;
    }

    // Método auxiliar para evitar bloqueio local de navegadores no desenvolvimento
    private void liberacaoCors(HttpServletResponse response) {
        if (response.getHeader("Access-Control-Allow-Origin") == null) {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        if (response.getHeader("Access-Control-Allow-Headers") == null) {
            response.addHeader("Access-Control-Allow-Headers", "*");
        }
        if (response.getHeader("Access-Control-Request-Headers") == null) {
            response.addHeader("Access-Control-Request-Headers", "*");
        }
        if (response.getHeader("Access-Control-Allow-Methods") == null) {
            response.addHeader("Access-Control-Allow-Methods", "*");
        }
    }
}
