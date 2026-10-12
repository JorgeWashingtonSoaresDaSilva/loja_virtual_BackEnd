package com.jwss.studios.loja_virtual_BackEnd.repository;

import com.jwss.studios.loja_virtual_BackEnd.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "select u from Usuario u where u.login = ?1")
    Usuario findUserByLogin(String login);

    @Query(value = "SELECT * FROM usuario WHERE data_atual_senha <= CURRENT_DATE - INTERVAL '90 days'", nativeQuery = true)
    List<Usuario> usuarioSenhaVencida();

    @Query(value = "select u from Usuario u where u.pessoa.id = ?1 or u.login = ?2")
    Usuario findByPessoa(Long id, String email);

    @Query(value = "select constraint_name from information_schema.constraint_column_usage\n" +
            "where table_name = 'usuarios_acesso' and column_name = 'acesso_id' and\n" +
            "    constraint_name<>'unique_acesso_user' ;", nativeQuery = true)
    String ConsultaConstraintAcesso();
    @Transactional
    @Modifying
    @Query(value = "insert into usuarios_acesso(usuario_id, acesso_id) values (?, (select id from acesso where descricao = 'ROLE_USER'))", nativeQuery = true)
    void insereAcessoUserPj(Long idUser);

    @Transactional
    @Modifying
    @Query(value = "insert into usuarios_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where descricao = ?2))", nativeQuery = true)
    void insereAcessoUserPj(Long idUser, String acesso);
}

