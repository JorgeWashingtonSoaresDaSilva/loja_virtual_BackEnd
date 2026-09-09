package com.jwss.studios.loja_virtual_BackEnd.service;

import com.jwss.studios.loja_virtual_BackEnd.model.Usuario;
import com.jwss.studios.loja_virtual_BackEnd.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findUserByLogin(username); // recebe login para consulta
        if (usuario == null){
            throw new UsernameNotFoundException("Usuário não foi encontrado");
        }
        return new User(usuario.getUsername(),usuario.getPassword(),usuario.getAuthorities());
    }
}
