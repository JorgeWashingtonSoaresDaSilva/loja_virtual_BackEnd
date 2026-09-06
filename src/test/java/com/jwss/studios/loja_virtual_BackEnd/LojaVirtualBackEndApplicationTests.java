package com.jwss.studios.loja_virtual_BackEnd;

import com.jwss.studios.loja_virtual_BackEnd.controller.AcessoController;
import com.jwss.studios.loja_virtual_BackEnd.model.Acesso;
import com.jwss.studios.loja_virtual_BackEnd.repository.AcessoRepository;
import com.jwss.studios.loja_virtual_BackEnd.service.AcessoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes =  LojaVirtualBackEndApplication.class)
class LojaVirtualBackEndApplicationTests {

	@Autowired
	private AcessoController acessoController;
	@Test
	public void testeCadastraAcesso() {

		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_AUXILIAR");
		//acessoRepository.salvarAcesso(acesso);
		//acessoService.salvarAcesso(acesso);
		acessoController.salvarAcesso(acesso);

	}

}
