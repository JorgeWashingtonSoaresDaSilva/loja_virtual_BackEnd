package com.jwss.studios.loja_virtual_BackEnd;

import com.jwss.studios.loja_virtual_BackEnd.controller.AcessoController;
import com.jwss.studios.loja_virtual_BackEnd.model.Acesso;
import com.jwss.studios.loja_virtual_BackEnd.repository.AcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes =  LojaVirtualBackEndApplication.class)
class LojaVirtualBackEndApplicationTests {

	@Autowired
	private AcessoController acessoController;

	@Autowired
	private AcessoRepository acessoRepository;

	@Test
	public void testeCadastraAcesso() {

		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_ADMIN");
		// gravou no banco
		acesso = acessoController.salvarAcesso(acesso).getBody();
		//ID > 0
		Assertions.assertEquals(true,acesso.getId() > 0);
		// validar dados salvo da forma correta
		Assertions.assertEquals("ROLE_ADMIN",acesso.getDescricao());

		//Teste de carregamento
		Optional<Acesso> acesso1 = acessoRepository.findById(acesso.getId());
		Assertions.assertEquals(acesso.getId(), acesso1.get().getId());

		// teste de delete
		acessoRepository.deleteById(acesso1.get().getId());
		acessoRepository.flush();// roda esse SQL de delete no banco de dados
		Acesso acesso2 = acessoRepository.findById(acesso1.get().getId()).orElse(null);
		Assertions.assertEquals(true, acesso2 == null);

		// teste de query
		acesso = new Acesso();
		acesso.setDescricao("ROLE_QUERY");
		acesso = acessoController.salvarAcesso(acesso).getBody();

		List<Acesso> acessos = acessoRepository.buscarAcessoDescricao("QUERY".trim().toUpperCase());
		Assertions.assertEquals(1,acessos.size());

		acessoRepository.deleteById(acesso.getId());


	}

}
