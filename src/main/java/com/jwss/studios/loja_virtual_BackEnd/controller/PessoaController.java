package com.jwss.studios.loja_virtual_BackEnd.controller;

import com.jwss.studios.loja_virtual_BackEnd.ExceptionMentoriaJava;
import com.jwss.studios.loja_virtual_BackEnd.model.PessoaJuridica;
import com.jwss.studios.loja_virtual_BackEnd.repository.PessoaRepository;
import com.jwss.studios.loja_virtual_BackEnd.service.PessoaUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PessoaController {
    @Autowired
    private PessoaUsuarioService pessoaUsuarioService;
    @Autowired
    private PessoaRepository pessoaRepository;

    @ResponseBody
    @PostMapping(value = "**/salvarPj")
    public ResponseEntity<PessoaJuridica>salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionMentoriaJava {
        if (pessoaJuridica == null){
            throw new ExceptionMentoriaJava("Pessoa juridíca não pode ser null");
        }
        if (pessoaJuridica.getId() == null && pessoaRepository.existeCNPJCadastrado(pessoaJuridica.getCnpj()) != null) {
          throw  new ExceptionMentoriaJava("Já exíste CNPJ cadastrado com esse número:"+ pessoaJuridica.getCnpj());
        }
        pessoaJuridica = pessoaUsuarioService.salvarPessoaJuridica(pessoaJuridica);

        return new ResponseEntity<>(pessoaJuridica, HttpStatus.OK);
    }


}
