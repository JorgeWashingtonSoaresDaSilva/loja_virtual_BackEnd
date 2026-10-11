package com.jwss.studios.loja_virtual_BackEnd.controller;

import com.jwss.studios.loja_virtual_BackEnd.ExceptionMentoriaJava;
import com.jwss.studios.loja_virtual_BackEnd.model.Acesso;
import com.jwss.studios.loja_virtual_BackEnd.service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RestController
public class AcessoController {
    @Autowired
    private AcessoService acessoService;

    @ResponseBody// da retorno da API
    @PostMapping(value = "**/salvarAcesso") // Mapeando url para receber JSON
    public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionMentoriaJava {// recebe o JSON converte para objeto
      if (acesso.getId() == null){
          List<Acesso> acessos = acessoService.buscarPorDescricao(acesso.getDescricao().toUpperCase());
          if (!acessos.isEmpty()){
              throw new ExceptionMentoriaJava("Já existe acesso com a descrição: "+ acesso.getDescricao());
          }
      }

        Acesso acessoSalvo = acessoService.salvarAcesso(acesso);
        return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
    }

    @ResponseBody// da retorno da API
    @DeleteMapping(value = "**/deletarAcesso") // Mapeando url para receber JSON
    public ResponseEntity<?> deletarAcesso(@RequestBody Acesso acesso) {// recebe o JSON converte para objeto
        acessoService.deletarAcesso(acesso.getId());
        return new ResponseEntity("Acesso removido", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "**/deletarAcessoPorId/{id}")
    public ResponseEntity<?> deletarAcessoPorId(@PathVariable("id") Long id) {
        acessoService.deletarAcesso(id);
        return new ResponseEntity("Acesso removido", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "**/obterAcessoPorId/{id}")
    public ResponseEntity<Acesso> obterAcessoPorId(@PathVariable("id") Long id) throws ExceptionMentoriaJava, IOException {
        Acesso acesso = acessoService.obterAcessoPorId(id).orElse(null);
        String msg = "Não encontrou Acesso com código:";
        if (acesso == null) {
            throw new ExceptionMentoriaJava(msg + id);

        }

        return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "**/buscarPorDescricao/{descricao}")
    public ResponseEntity<List<Acesso>> buscarPorDescricao(@PathVariable("descricao") String descricao) {
        List<Acesso> acesso = acessoService.buscarPorDescricao(descricao);
        return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
    }


}
