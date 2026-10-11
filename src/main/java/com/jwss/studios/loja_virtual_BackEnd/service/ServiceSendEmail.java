package com.jwss.studios.loja_virtual_BackEnd.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class ServiceSendEmail {

    @Async
    public void enviarEmailHtml(String assunto, String mensagem, String emailDestino) throws MessagingException, UnsupportedEncodingException {
        /* Olhe as configurações smtp do seu email */
        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.auth", "true");/* Autorização */
        properties.put("mail.smtp.starttls.enable", "false");/* Autenticação */
        properties.put("mail.smtp.host", "smtp.gmail.com");/* Servidor do gmail do Google */
        properties.put("mail.smtp.port", "465");/* Porta do servidor */
        properties.put("mail.smtp.socketFactory.port", "465"); /* Expecifica a porta a ser connectada pelo socket */
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");/* Classe socket de conexão ao SMTP */

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // retornando new passwordAutentication
                return new PasswordAuthentication(userName, password);
            }
        });
        session.setDebug(true);

        Address[] toUser = InternetAddress.parse(emailDestino);

        Message menssage = new MimeMessage(session);
        menssage.setFrom(new InternetAddress(userName,"Jorge Wahington","UTF-8")); /* Quem esta enviando */
        menssage.setRecipients(Message.RecipientType.TO, toUser);/* Email de destino */
        menssage.setSubject(assunto);/* Asssunto do e-mail */
        menssage.setContent(mensagem,"text/html;charset=utf-8");

        Transport.send(menssage);
    }

}
