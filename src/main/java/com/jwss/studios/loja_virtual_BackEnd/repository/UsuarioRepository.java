package com.jwss.studios.loja_virtual_BackEnd.repository;

import com.jwss.studios.loja_virtual_BackEnd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    @Query(value = "select u from Usuario u where u.login = ?1")
    Usuario findUserByLogin(String login);
}
