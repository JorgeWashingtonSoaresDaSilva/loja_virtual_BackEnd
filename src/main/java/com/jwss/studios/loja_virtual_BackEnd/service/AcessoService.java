package com.jwss.studios.loja_virtual_BackEnd.service;

import com.jwss.studios.loja_virtual_BackEnd.model.Acesso;
import com.jwss.studios.loja_virtual_BackEnd.repository.AcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcessoService {

    @Autowired
    private AcessoRepository acessoRepository;

    public Acesso salvarAcesso(Acesso acesso){
        // qualquer tipo de validação antes de salvar é feito aqui
        return  acessoRepository.save(acesso);
    }

    public void  deletarAcesso(Long id){
        // qualquer tipo de validação antes de salvar é feito aqui
       acessoRepository.deleteById(id);
    }

}
