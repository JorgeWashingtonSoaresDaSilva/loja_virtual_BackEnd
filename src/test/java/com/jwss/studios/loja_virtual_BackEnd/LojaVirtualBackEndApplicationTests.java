package com.jwss.studios.loja_virtual_BackEnd;

import com.jwss.studios.loja_virtual_BackEnd.controller.AcessoController;
import com.jwss.studios.loja_virtual_BackEnd.model.Acesso;
import com.jwss.studios.loja_virtual_BackEnd.repository.AcessoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Profile("test")
@SpringBootTest(classes = LojaVirtualBackEndApplication.class)
class LojaVirtualBackEndApplicationTests {

    @Autowired
    private AcessoController acessoController;

    @Autowired
    private AcessoRepository acessoRepository;
    @Autowired
    private WebApplicationContext wac;

    // Teste do end-point de salvar
    @Test
    public void testeRestApiCadastroAcesso() throws Exception {
        //Tras todas as informações e testes
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();
        // criando dados de teste e salvando
        Acesso acesso = new Acesso();

        acesso.setDescricao("ROLE_COMPRADOR");
        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.post("/salvarAcesso")
                .content(objectMapper.writeValueAsString(acesso))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
        //converter o retornoApi para um objeto de acesso
        Acesso objetoRetorno = objectMapper.
                readValue(retornoApi.andReturn().getResponse().getContentAsString(),
                        Acesso.class);
        Assertions.assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());


    }

    // Teste do end-point obterAcesso
    @Test
    public void testeRestApiObterAcessoPorId() throws Exception {
        //Tras todas as informações e testes
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();
        // criando dados de teste e salvando
        Acesso acesso = new Acesso();

        acesso.setDescricao("ROLE_TESTE_OBTER_ID");
        acesso = acessoRepository.save(acesso);
        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.get("/obterAcessoPorId/" + acesso.getId())
                .content(objectMapper.writeValueAsString(acesso))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        Assertions.assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
        //Convertemos para objeto fazer os testes
        Acesso acessoRetornoApi = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
        Assertions.assertEquals(acesso.getDescricao(), acessoRetornoApi.getDescricao());
        Assertions.assertEquals(acesso.getId(), acessoRetornoApi.getId());

    }

    // Teste do end-point buacarAcessoPorDescrição
    @Test
    public void testeRestApiBuscarAcessoPorDescricao() throws Exception {
        //Tras todas as informações e testes
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();
        // criando dados de teste e salvando
        Acesso acesso = new Acesso();

        acesso.setDescricao("ROLE_TESTE_BUSCAR_DESCRICAO");
        acesso = acessoRepository.save(acesso);
        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.get("/buscarPorDescricao/BUSCAR_DESCRICAO")
                .content(objectMapper.writeValueAsString(acesso))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        Assertions.assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
        List<Acesso> retornoAPILista = objectMapper.readValue(retornoApi.andReturn()
                        .getResponse().getContentAsString(),
                new TypeReference<List<Acesso>>() {
                });
        Assertions.assertEquals(1, retornoAPILista.size());

        Assertions.assertEquals(acesso.getDescricao(), retornoAPILista.get(0).getDescricao());

        acessoRepository.deleteById(acesso.getId());

    }

    // Teste do end-point delete
    @Test
    public void testeRestApiDeleteAcesso() throws Exception {
        //Tras todas as informações e testes
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();
        // criando dados de teste e salvando
        Acesso acesso = new Acesso();

        acesso.setDescricao("ROLE_TESTE_DELETE");
        acesso = acessoRepository.save(acesso);
        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.delete("/deletarAcesso")
                .content(objectMapper.writeValueAsString(acesso))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
        System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());

        Assertions.assertEquals("Acesso removido", retornoApi.andReturn().getResponse().getContentAsString());
        Assertions.assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

    }

    // Teste do end-point delete
    @Test
    public void testeRestApiDeletePorIDAcesso() throws Exception {
        //Tras todas as informações e testes
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();
        // criando dados de teste e salvando
        Acesso acesso = new Acesso();

        acesso.setDescricao("ROLE_TESTE_DELETE_ID");
        acesso = acessoRepository.save(acesso);
        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.delete("/deletarAcessoPorId/" + acesso.getId())
                .content(objectMapper.writeValueAsString(acesso))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
        System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
        System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());

        Assertions.assertEquals("Acesso removido", retornoApi.andReturn().getResponse().getContentAsString());
        Assertions.assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

    }


    @Test
    public void testeCadastraAcesso() {

        Acesso acesso = new Acesso();
        acesso.setDescricao("ROLE_ADMIN");
        // gravou no banco
        acesso = acessoController.salvarAcesso(acesso).getBody();
        //ID > 0
        Assertions.assertEquals(true, acesso.getId() > 0);
        // validar dados salvo da forma correta
        Assertions.assertEquals("ROLE_ADMIN", acesso.getDescricao());

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
        Assertions.assertEquals(1, acessos.size());

        acessoRepository.deleteById(acesso.getId());


    }

}
