package com.jwss.studios.loja_virtual_BackEnd.controller;

import com.jwss.studios.loja_virtual_BackEnd.ExceptionMentoriaJava;
import com.jwss.studios.loja_virtual_BackEnd.model.Endereco;
import com.jwss.studios.loja_virtual_BackEnd.model.PessoaFisica;
import com.jwss.studios.loja_virtual_BackEnd.model.PessoaJuridica;
import com.jwss.studios.loja_virtual_BackEnd.model.dto.CepDTO;
import com.jwss.studios.loja_virtual_BackEnd.repository.EnderecoRepository;
import com.jwss.studios.loja_virtual_BackEnd.repository.PessoaRepository;
import com.jwss.studios.loja_virtual_BackEnd.service.PessoaUsuarioService;
import com.jwss.studios.loja_virtual_BackEnd.util.ValidaCNPJ;
import com.jwss.studios.loja_virtual_BackEnd.util.ValidaCPF;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PessoaController {
    @Autowired
    private PessoaUsuarioService pessoaUsuarioService;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;

    @ResponseBody
    @GetMapping(value = "**/consultaCep/{cep}")
    public ResponseEntity<CepDTO>consultaCep(@PathVariable("cep") String cep ){
       CepDTO cepDTO = pessoaUsuarioService.consultaCep(cep);

       return new ResponseEntity<>(cepDTO, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "**/salvarPj")
    public ResponseEntity<PessoaJuridica>salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionMentoriaJava {


        if (pessoaJuridica == null){
            throw new ExceptionMentoriaJava("Pessoa juridíca não pode ser null");
        }
        if (pessoaJuridica.getId() == null && pessoaRepository.existeCNPJCadastrado(pessoaJuridica.getCnpj()) != null) {
          throw  new ExceptionMentoriaJava("Já exíste CNPJ cadastrado com esse número:"+ pessoaJuridica.getCnpj());
        }
        if (pessoaJuridica.getId() == null && pessoaRepository.existeInscEstatualCadastrado(pessoaJuridica.getInscEstatual()) != null) {
            throw  new ExceptionMentoriaJava("Já exíste InscEstatual cadastrado com esse número:"+ pessoaJuridica.getInscEstatual());
        }
        if(!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())){
            throw  new ExceptionMentoriaJava("Cnpj: "+ pessoaJuridica.getCnpj()+"Está inválido");
        }
        if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0){
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++){
                CepDTO cepDTO = pessoaUsuarioService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
                pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
                pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
                pessoaJuridica.getEnderecos().get(p).setRuaLugra(cepDTO.getLogradouro());
                pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
                pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
            }
        }else {
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++){
                Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();
                if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())){

                    CepDTO cepDTO = pessoaUsuarioService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());

                    pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
                    pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
                    pessoaJuridica.getEnderecos().get(p).setRuaLugra(cepDTO.getLogradouro());
                    pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
                    pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
                }
            }
        }

        pessoaJuridica = pessoaUsuarioService.salvarPessoaJuridica(pessoaJuridica);

        return new ResponseEntity<>(pessoaJuridica, HttpStatus.OK);
    }
    @ResponseBody
    @PostMapping(value = "**/salvarPf")
    public ResponseEntity<PessoaFisica>salvarPf(@RequestBody @Valid PessoaFisica pessoaFisica) throws ExceptionMentoriaJava {


        if (pessoaFisica == null){
            throw new ExceptionMentoriaJava("Pessoa fisica não pode ser null");
        }
        if (pessoaFisica.getId() == null && pessoaRepository.existeCPFCadastrado(pessoaFisica.getCPF()) != null) {
            throw  new ExceptionMentoriaJava("Já exíste CPF cadastrado com esse número:"+ pessoaFisica.getCPF());
        }
        if(!ValidaCPF.isCPF(pessoaFisica.getCPF())){
            throw  new ExceptionMentoriaJava("CPF: "+ pessoaFisica.getCPF()+"Está inválido");
        }

        pessoaFisica = pessoaUsuarioService.salvarPessoaFisica(pessoaFisica);

        return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
    }



}
