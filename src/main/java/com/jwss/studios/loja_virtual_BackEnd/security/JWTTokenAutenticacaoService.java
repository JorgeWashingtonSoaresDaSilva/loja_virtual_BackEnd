package com.jwss.studios.loja_virtual_BackEnd.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTTokenAutenticacaoService {

    // Token de validade de 15 dias
    private static final long EXPIRATION_TIME = 15L * 24 * 60 * 60 * 1000;
    //Chave de senha para juntar com JWT
    private static final String SECRET ="palava secreta";
    // Prefixo do token
    private static final String TOKEN_PREFIX = "Bearer";
    // Autorization
    private static final String HEADER_STRING = "Authorization";

    // Gera o token e da a resposta para o cliente com JWT
    public void addAuthentication(HttpServletResponse response,String username) throws Exception{
      System.out.println(EXPIRATION_TIME);
        // montagem do token
        String JWT = Jwts.builder()// chama gerador de token
                .setSubject(username) // adiciona o username
                .setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))// Tempo de expiração
                .signWith(SignatureAlgorithm.HS512,SECRET).compact();
        // Exemplo: Bearer FFFGFGHN.HJKKKKK<K>LK.JMGHMGHMHGMG.defrefwefef
        String token = TOKEN_PREFIX +" "+ JWT;

        // retorna para tela com token no navegador, aplicativo, javaScript, chamada java
        response.addHeader(HEADER_STRING,token);
        // Usado no postman para teste
        response.getWriter().write("{\"Authorization\":\""+token+"\"}");
    }

}
