package com.jwss.studios.loja_virtual_BackEnd.service;

import com.jwss.studios.loja_virtual_BackEnd.model.Endereco;
import com.jwss.studios.loja_virtual_BackEnd.model.PessoaFisica;
import com.jwss.studios.loja_virtual_BackEnd.model.PessoaJuridica;
import com.jwss.studios.loja_virtual_BackEnd.model.Usuario;
import com.jwss.studios.loja_virtual_BackEnd.repository.PessoaFisicaRepository;
import com.jwss.studios.loja_virtual_BackEnd.repository.PessoaRepository;
import com.jwss.studios.loja_virtual_BackEnd.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class PessoaUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ServiceSendEmail serviceSendEmail;
    // 1. INJETE O ENTITYMANAGER AQUI:
    @jakarta.persistence.PersistenceContext
    private jakarta.persistence.EntityManager entityManager;

    @Transactional
    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica){

        // 1. Isola os endereços temporariamente para evitar cascata prematura
        List<Endereco> enderecosTemporarios = pessoaJuridica.getEnderecos();
        pessoaJuridica.setEnderecos(new java.util.ArrayList<>());

        // 2. Garante que o campo empresa está null para o primeiro Insert passar livre pelo JPA
        pessoaJuridica.setEmpresa(null);

        // 3. Salva a Pessoa Jurídica no banco (Gera o ID na tabela pessoa e pessoa_juridica)
        pessoaJuridica = pessoaRepository.saveAndFlush(pessoaJuridica);

        // 4. Agora que ela possui ID no banco, usamos o JdbcTemplate para injetar o empresa_id nela mesma
        // Isso evita que o JPA reclame de objeto transiente ou cíclico
        String sqlUpdateEmpresa = "UPDATE pessoa SET empresa_id = ? WHERE id = ?";
        jdbcTemplate.update(sqlUpdateEmpresa, pessoaJuridica.getId(), pessoaJuridica.getId());

        // Sincroniza o estado no objeto Java atualizado
        pessoaJuridica.setEmpresa(pessoaJuridica);

        // 5. Vincula os endereços agora que a pessoa possui ID e Empresa válidos no banco
        if (enderecosTemporarios != null && !enderecosTemporarios.isEmpty()) {
            for (Endereco end : enderecosTemporarios) {
                end.setPessoa(pessoaJuridica);
                end.setEmpresa(pessoaJuridica); // Vincula à empresa criada
            }
            // Devolve os endereços para a entidade
            pessoaJuridica.getEnderecos().addAll(enderecosTemporarios);

            // Salva os endereços via cascata atualizando a árvore do JPA
            pessoaJuridica = pessoaRepository.saveAndFlush(pessoaJuridica);
        }

        // Restante do seu código original (Geração de usuário u1_0, envio de e-mail, etc) ...
        Usuario usuarioPj = usuarioRepository.findByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

        if (usuarioPj == null) {
            // consulta se tem uma constraint no banco
            String constraint = usuarioRepository.ConsultaConstraintAcesso();
            if (constraint != null){
               // remove a constraint do banco
                jdbcTemplate.execute("alter table usuarios_acesso drop constraint "+constraint);
            }
            // Preenche um usuario para salvar no banco
            usuarioPj = new Usuario();
            usuarioPj.setDataAtualSenha(LocalDate.now());
            usuarioPj.setEmpresa(pessoaJuridica);
            usuarioPj.setPessoa(pessoaJuridica);
            usuarioPj.setLogin(pessoaJuridica.getEmail());
            // gera uma senha com calendar
            String senha = ""+ Calendar.getInstance().getTimeInMillis();
            // criptografa a senha para salvar no banco
            String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);
            usuarioPj.setSenha(senhaCriptografada);

            // salva novo usuario no banco
            usuarioPj = usuarioRepository.save(usuarioPj);

            // cadastra o acesso novo usuario
            usuarioRepository.insereAcessoUser(usuarioPj.getId());
            usuarioRepository.insereAcessoUser(usuarioPj.getId(),"ROLE_ADMIN");
           // mensagem do email
            StringBuilder mensagemHtml = new StringBuilder();
            mensagemHtml.append("<b>Segue abaixo seus dados de acesso para loja virtual</b><br/>");
            mensagemHtml.append("<b>Login:</b>"+pessoaJuridica.getEmail()+" <br/>");
            mensagemHtml.append("<b>Senha: </b> "+senha+"<br/><br/>");
            mensagemHtml.append("Obrigado!");

            // fazer envio de email do login e senha
          try {
              serviceSendEmail.enviarEmailHtml("Acessso gerado para Loja Virtual",mensagemHtml.toString(), pessoaJuridica.getEmail());
          }catch (Exception e){
              e.printStackTrace();
          }



        }
        // 2. ADICIONE ESSA ATUALIZAÇÃO ANTES DO RETURN:
        // Salva qualquer estado pendente e força o Hibernate a recarregar a árvore
        // de relacionamentos polimórficos limpa do banco de dados, matando o erro do Jackson.
        pessoaRepository.saveAndFlush(pessoaJuridica);
        entityManager.refresh(pessoaJuridica);
        return pessoaJuridica;
    }

    @Transactional
    public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {

        // 1. Isola os endereços temporariamente para evitar problemas na árvore do JPA
        List<Endereco> enderecosTemporarios = pessoaFisica.getEnderecos();
        pessoaFisica.setEnderecos(new ArrayList<>());

        // 2. SALVA A PESSOA FÍSICA PRIMEIRO (Necessário para gerar o ID que o usuário precisa)
        pessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Busca se já existe um usuário vinculado
        Usuario usuarioPf = usuarioRepository.findByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

        if (usuarioPf == null) {
            // Consulta se tem uma constraint temporária no banco
            String constraint = usuarioRepository.ConsultaConstraintAcesso();
            if (constraint != null){
                // Remove a constraint do banco
                jdbcTemplate.execute("alter table usuarios_acesso drop constraint " + constraint);
            }

            // Preenche o usuário para salvar no banco
            usuarioPf = new Usuario();
            usuarioPf.setDataAtualSenha(LocalDate.now());
            usuarioPf.setEmpresa(pessoaFisica.getEmpresa()); // Corrigido: pegando a Empresa Jurídica
            usuarioPf.setPessoa(pessoaFisica);
            usuarioPf.setLogin(pessoaFisica.getEmail());

            // Gera uma senha baseada no Timestamp
            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);
            usuarioPf.setSenha(senhaCriptografada);

            // Salva o novo usuário no banco
            usuarioPf = usuarioRepository.save(usuarioPf);

            // Cadastra o acesso padrão do novo usuário
            usuarioRepository.insereAcessoUser(usuarioPf.getId());

            // Corpo do e-mail
            StringBuilder mensagemHtml = new StringBuilder();
            mensagemHtml.append("<b>Segue abaixo seus dados de acesso para loja virtual</b><br/>");
            mensagemHtml.append("<b>Login:</b>" + pessoaFisica.getEmail() + " <br/>");
            mensagemHtml.append("<b>Senha: </b> " + senha + "<br/><br/>");
            mensagemHtml.append("Obrigado!");

            // Envio do e-mail assíncrono/seguro
            try {
                serviceSendEmail.enviarEmailHtml("Acesso gerado para Loja Virtual", mensagemHtml.toString(), pessoaFisica.getEmail());
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        // 3. RECUPERA OS ENDEREÇOS E ATRELA À PESSOA JÁ SALVA (Evita Transient Object Exception)
        if (enderecosTemporarios != null && !enderecosTemporarios.isEmpty()) {
            for (Endereco endereco : enderecosTemporarios) {
                endereco.setPessoa(pessoaFisica); // Garante o vínculo do ID da pessoa no endereço
                endereco.setEmpresa(pessoaFisica.getEmpresa()); // Garante o vínculo da empresa no endereço
            }
            pessoaFisica.getEnderecos().addAll(enderecosTemporarios);
        }

        // 4. SALVA TUDO EM DEFINITIVO (Isso vai disparar o Cascade para salvar os endereços no banco)
        pessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Atualiza a sessão do Hibernate limpa
        entityManager.refresh(pessoaFisica);

        return pessoaFisica;
    }

}
