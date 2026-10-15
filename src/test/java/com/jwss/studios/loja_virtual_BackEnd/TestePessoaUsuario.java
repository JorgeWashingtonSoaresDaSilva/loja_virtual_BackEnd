package com.jwss.studios.loja_virtual_BackEnd;

import com.jwss.studios.loja_virtual_BackEnd.controller.PessoaController;
import com.jwss.studios.loja_virtual_BackEnd.enums.TipoEndereco;
import com.jwss.studios.loja_virtual_BackEnd.model.Endereco;
import com.jwss.studios.loja_virtual_BackEnd.model.PessoaFisica;
import com.jwss.studios.loja_virtual_BackEnd.model.PessoaJuridica;
import com.jwss.studios.loja_virtual_BackEnd.repository.PessoaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.Calendar;

@Profile("test")
@SpringBootTest(classes = LojaVirtualBackEndApplication.class)
public class TestePessoaUsuario {
    @Autowired
    private PessoaController pessoaController;
    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    public void testeCadastroPessoaJuridica() throws ExceptionMentoriaJava {
        //PessoaJuridica
        PessoaJuridica pessoaJuridica = new PessoaJuridica();

        pessoaJuridica.setCnpj(""+ Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setNome("Jorge ");
        pessoaJuridica.setEmail("js486washington@gmail.com");
        pessoaJuridica.setTelefone("4564567800");
        pessoaJuridica.setInscEstatual("4545s656");
        pessoaJuridica.setInscMunicipal("3434357877678");
        pessoaJuridica.setNomeFantasia("Tesrrtr Empresa");
        pessoaJuridica.setRazaoSocial("Empresa de Tesdfdf");
        pessoaJuridica.setEmpresa(pessoaJuridica);
        pessoaJuridica.setTipoPessoa("JURIDICA");
        Endereco endereco1 = new Endereco();
        endereco1.setRuaLugra("rua 1");
        endereco1.setNumero("45");
        endereco1.setComplemento("casa1");
        endereco1.setBairro("Boa vista");
        endereco1.setCidade("suazano");
        endereco1.setCep("87878634");
        endereco1.setUf("são paulo");
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
        endereco1.setEmpresa(pessoaJuridica);
        endereco1.setPessoa(pessoaJuridica);
        Endereco endereco2 = new Endereco();
        endereco2.setRuaLugra("rua 2");
        endereco2.setNumero("49");
        endereco2.setComplemento("casa2");
        endereco2.setBairro("Boa vista");
        endereco2.setCidade("suazano");
        endereco2.setCep("878732334");
        endereco2.setUf("são paulo");
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
        endereco2.setEmpresa(pessoaJuridica);
        endereco2.setPessoa(pessoaJuridica);

        pessoaJuridica.getEnderecos().add(endereco1);
        pessoaJuridica.getEnderecos().add(endereco2);
        pessoaController.salvarPj(pessoaJuridica).getBody();
        Assertions.assertEquals(true, pessoaJuridica.getId() > 0);

        for (Endereco endereco : pessoaJuridica.getEnderecos() ){
            Assertions.assertEquals(true, endereco.getId() > 0);
        }
        Assertions.assertEquals(2, pessoaJuridica.getEnderecos().size());


    }
    @Test
    public void testeCadastroPessoaFisica() throws ExceptionMentoriaJava {
        PessoaJuridica pessoaJuridica = pessoaRepository.existeCNPJCadastrado("86275669000101");
        PessoaFisica pessoafisica = new  PessoaFisica();

        pessoafisica.setCPF("850.610.140-90");
        pessoafisica.setNome("");
        pessoafisica.setDataNascimento(LocalDate.of(1994, 3, 17));
        pessoafisica.setEmail("js486shington@gmail.com");
        pessoafisica.setTelefone("4564567800");
        pessoafisica.setEmpresa(pessoaJuridica);
        pessoafisica.setTipoPessoa("FISICA");
        Endereco endereco1 = new Endereco();
        endereco1.setRuaLugra("rua 1");
        endereco1.setNumero("45");
        endereco1.setComplemento("casa1");
        endereco1.setBairro("Boa vista");
        endereco1.setCidade("suazano");
        endereco1.setCep("87878634");
        endereco1.setUf("são paulo");
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
        endereco1.setEmpresa(pessoaJuridica);
        endereco1.setPessoa(pessoafisica);
        Endereco endereco2 = new Endereco();
        endereco2.setRuaLugra("rua 2");
        endereco2.setNumero("49");
        endereco2.setComplemento("casa2");
        endereco2.setBairro("Boa vista");
        endereco2.setCidade("suazano");
        endereco2.setCep("878732334");
        endereco2.setUf("são paulo");
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
        endereco2.setEmpresa(pessoaJuridica);
        endereco2.setPessoa(pessoafisica);

        pessoafisica.getEnderecos().add(endereco1);
        pessoafisica.getEnderecos().add(endereco2);
        pessoaController.salvarPf(pessoafisica).getBody();
        Assertions.assertEquals(true, pessoafisica.getId() > 0);

        for (Endereco endereco : pessoafisica.getEnderecos() ){
            Assertions.assertEquals(true, endereco.getId() > 0);
        }
        Assertions.assertEquals(2, pessoafisica.getEnderecos().size());


    }



}
