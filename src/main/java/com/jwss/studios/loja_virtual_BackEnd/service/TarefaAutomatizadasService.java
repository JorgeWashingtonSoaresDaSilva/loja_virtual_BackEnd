package com.jwss.studios.loja_virtual_BackEnd.service;

import com.jwss.studios.loja_virtual_BackEnd.model.Usuario;
import com.jwss.studios.loja_virtual_BackEnd.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class TarefaAutomatizadasService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @Scheduled(initialDelay = 2000,fixedDelay = 86400000)/* Roda a cada 24 horas */
   // @Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") /* Vai rodar todo dia as 11 horas da manhã*/
    public void notificarUserTrocaSenha() throws MessagingException, UnsupportedEncodingException, InterruptedException {

        List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();
        for (Usuario usuario : usuarios){
            StringBuilder msg = new StringBuilder();
            msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("<br/>");
            msg.append("Esta na hora de trocar sua senha, já passou 90 dias de validade").append("<br/>");
            msg.append("Troque sua senha da loja virtual do Jwss Studios");

            serviceSendEmail.enviarEmailHtml("Troca de senha",msg.toString(),usuario.getLogin());
            Thread.sleep(3000);

        }

    }

}
