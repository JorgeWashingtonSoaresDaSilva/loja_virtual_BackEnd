package com.jwss.studios.loja_virtual_BackEnd.service;

import com.jwss.studios.loja_virtual_BackEnd.model.PessoaJuridica;
import com.jwss.studios.loja_virtual_BackEnd.model.Usuario;
import com.jwss.studios.loja_virtual_BackEnd.repository.PessoaRepository;
import com.jwss.studios.loja_virtual_BackEnd.repository.UsuarioRepository;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Calendar;

@Service
public class PessoaUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica){
        pessoaJuridica =  pessoaRepository.save(pessoaJuridica);
        Usuario usuarioPj = usuarioRepository.findByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

        if (usuarioPj == null){
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
            usuarioRepository.insereAcessoUserPj(usuarioPj.getId());


        }
        return pessoaJuridica;
    }


}
