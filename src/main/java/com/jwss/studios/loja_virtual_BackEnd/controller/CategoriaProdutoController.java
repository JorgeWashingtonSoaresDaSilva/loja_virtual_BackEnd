package com.jwss.studios.loja_virtual_BackEnd.controller;

import com.jwss.studios.loja_virtual_BackEnd.ExceptionMentoriaJava;
import com.jwss.studios.loja_virtual_BackEnd.model.CategoriaProduto;
import com.jwss.studios.loja_virtual_BackEnd.model.dto.CategoriaProdutoDTO;
import com.jwss.studios.loja_virtual_BackEnd.repository.CategoriaProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CategoriaProdutoController {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @ResponseBody
    @PostMapping(value = "**/salvarCategoria")
    public ResponseEntity<CategoriaProdutoDTO> SalvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionMentoriaJava {

        // Valida se a empresa foi informada
        if (categoriaProduto.getEmpresa() == null || categoriaProduto.getEmpresa().getId() <= 0 || categoriaProduto.getEmpresa().getId() == null) {
            throw new ExceptionMentoriaJava("Empresa é obrigatória para cadastro de categoria de produto");
        }
        if(categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc().trim().toUpperCase())){
            throw new ExceptionMentoriaJava("Não pode cadastrar categoria com mesmo nome.");
        }

        CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);
        CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
        categoriaProdutoDTO.setId(categoriaSalva.getId());
        categoriaProdutoDTO.setNomeDesc(categoriaSalva.getNomeDesc());
        categoriaProdutoDTO.setEmpresa(categoriaSalva.getEmpresa().getId().toString());
        return new ResponseEntity<CategoriaProdutoDTO>(categoriaProdutoDTO, HttpStatus.OK);
    }
}
