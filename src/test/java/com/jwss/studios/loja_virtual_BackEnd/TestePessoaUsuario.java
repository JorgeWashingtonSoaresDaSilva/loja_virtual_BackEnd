package com.jwss.studios.loja_virtual_BackEnd;

import com.jwss.studios.loja_virtual_BackEnd.controller.PessoaController;
import com.jwss.studios.loja_virtual_BackEnd.model.Pessoa;
import com.jwss.studios.loja_virtual_BackEnd.model.PessoaJuridica;
import com.jwss.studios.loja_virtual_BackEnd.repository.PessoaRepository;
import com.jwss.studios.loja_virtual_BackEnd.service.PessoaUsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Calendar;

@Profile("test")
@SpringBootTest(classes = LojaVirtualBackEndApplication.class)
public class TestePessoaUsuario {
    @Autowired
    private PessoaController pessoaController;

    @Test
    public void testeCadastroPessoa() throws ExceptionMentoriaJava {
        //PessoaJuridica
        PessoaJuridica pessoaJuridica = new PessoaJuridica();

        pessoaJuridica.setCnpj(""+ Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setNome("Jorge Washongton");
        pessoaJuridica.setEmail("jwss486@gmail.com");
        pessoaJuridica.setTelefone("45677800");
        pessoaJuridica.setInscEstatual("45456546543656");
        pessoaJuridica.setInscMunicipal("343435467657678");
        pessoaJuridica.setNomeFantasia("Teste Empresa");
        pessoaJuridica.setRazaoSocial("Empresa de Teste");
        pessoaController.salvarPj(pessoaJuridica);

        /*/Pessoa Fisica
        PessoaFisica pessoaFisica = new PessoaFisica();
        pessoaFisica.setCPF("0597975788");
        pessoaFisica.setNome("Jorge Washongton");
        pessoaFisica.setEmail("jwss486@gmail.com");
        pessoaFisica.setTelefone("45677800");

        pessoaFisica.setEmpresa(p);*/
    }


}
