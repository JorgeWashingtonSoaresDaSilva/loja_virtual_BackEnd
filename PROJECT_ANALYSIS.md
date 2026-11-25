# Análise Completa do Projeto Loja Virtual BackEnd

## Visão Geral

O projeto **loja_virtual_BackEnd** é um aplicativo backend completo para uma loja virtual, construído com o ecossistema Spring Boot 4.0.6 e Java 21. Ele segue uma arquitetura em camadas com foco em segurança, escalabilidade e manipulação de dados complexos.

## Tecnologias Principais

- **Java 21** - Última versão LTS da linguagem Java
- **Spring Boot 4.0.6** - Framework base com muitas atualizações modernas
- **Spring Data JPA** - Abstração de banco de dados com Hibernate
- **Spring Data REST** - Exposição automática de repositórios como endpoints REST
- **Spring Security 7** - Segurança com autenticação JWT
- **PostgreSQL** - Banco de dados relacional
- **Flyway** - Migrações de banco de dados
- **Maven** - Gerenciador de build e dependências

## Arquitetura das Camadas

### 1. Controladores (Controllers)
- **AcessoController** - Gerencia permissões/acessos do sistema
- **PessoaController** - Gerencia cadastro de pessoas (físicas e jurídicas), consulta de CEP e CNPJ

### 2. Serviços (Services)
- **PessoaUsuarioService** - Serviço principal para cadastro de pessoas, geração de usuários e emails
- **AcessoService** - CRUD para acessos do sistema
- **TarefaAutomatizadasService** - Tarefas agendadas (verificação de senha vencida)
- **ServiceSendEmail** - Envio de emails (assíncrono com @Async)

### 3. Repositórios (Repositories)
- **PessoaRepository** - Queries customizadas para PJ e PF
- **UsuarioRepository** - Queries customizadas para autenticação
- **AcessoRepository** - Busca por descrição
- **EnderecoRepository** - Interface padrão JPA

### 4. Modelos (Entities)
O projeto possui 26 entidades principais:

| Entidade | Descrição |
|----------|-----------|
| `Usuario` | Usuário do sistema (implementa UserDetails) |
| `Pessoa` | Classe abstrata base para PJ e PF |
| `PessoaFisica` | Pessoa física com validação CPF |
| `PessoaJuridica` | Pessoa jurídica com validação CNPJ |
| `Acesso` | Permissões/roles do sistema |
| `Produto` | Produtos da loja com controle de estoque |
| `CategoriaProduto` | Categorias de produtos |
| `MarcaProduto` | Marcas de produtos |
| `FormaPagamento` | Formas de pagamento |
| `CupomDesconto` | Cupons de desconto |
| `Endereco` | Endereços com tipo (cobrança/entrega) |
| `NotaFiscalCompra` | Notas fiscais de compra |
| `NotaFiscalVenda` | Notas fiscais de venda |
| `VendaCompraLojaVirtual` | Vendas/compras da loja |
| `ItemVendaLoja` | Itens dentro de uma venda |
| `ContaPagar` | Contas a pagar |
| `ContaReceber` | Contas a receber |
| `StatusRastreio` | Status de rastreio de entregas |
| `ImagemProduto` | Imagens dos produtos |
| `AvaliacaoProduto` | Avaliações de produtos |

### 5. DTOs (Data Transfer Objects)
- `CepDTO` - Dados do CEPViaCEP
- `ConsultaCnpjDTO` - Dados completos da consulta CNPJ
- `SimplesDTO` - Simples Nacional
- `SimeiDTO` - Opção MEI
- `ExtraDTO` - Dados extras
- `BillingDTO` - Configurações de cobrança
- `ObjetoErroDTO` - Estrutura de erros
- `AtividadeDTO` - Atividades principais/secundárias

### 6. Utilitários
- `ValidaCPF` - Validação de CPF (algoritmo de verificação)
- `ValidaCNPJ` - Validação de CNPJ (algoritmo de verificação)

### 7. Segurança
- `WebConfigSecurity` - Configuração do SecurityFilterChain
- `JWTTokenAutenticacaoService` - Geração e validação de tokens JWT
- `JWTLoginFilter` - Filtro de login (POST /login)
- `JwtApiAuthenticacaoFilter` - Filtro de autenticação JWT para APIs

### 8. Enums
- `TipoPessoa` - FISICA, JURIDICA, JURIDICA_FORNECEDOR
- `StatusContaReceber` - COBRANCA, VENCIDA, ABERTA, QUITADA
- `StatusContaPagar` - COBRANCA, VENCIDA, ABERTA, QUITADA, ALUGUEL, FUNCIONARIO, NEGOCIADA
- `TipoEndereco` - COBRANCA, ENTREGA

## Banco de Dados

### Schema Principal
23 tabelas com relacionamentos complexos:

- **Core entities**: pessoa, usuario, acesso, pessoa_fisica, pessoa_juridica
- **Commercial**: produto, categoria_produto, marca_produto, forma_pagamento, cupom_desconto
- **Sales**: venda_compra_loja_virtual, item_venda_loja, nota_fiscal_venda, nota_fiscal_compra
- **Financial**: conta_pagar, conta_receber, status_rastreio
- **Support**: endereco, avaliacao_produto, imagem_produto

### Sequências
27 sequências para geração de IDs em todas as tabelas.

### Migrações (Flyway)
- **V1__init.sql** - Script completo de inicialização (create schema, tables, data, sequences)
- **V2__23-08-2026.sql** - Script de alteração (drop constraint temporário)

## Pontos Fortes

1. **Arquitetura Completa**: Todas as camadas bem definidas e separadas
2. **Segurança JWT Implementada**: Configuração correta do Spring Security 7 com filtros personalizados
3. **Validações de Dados**: Validação CPF/CNPJ nativa com algoritmos de verificação
4. **Relacionamentos Complexos**: Herança JOAOIN (Pessoa -> PJ/PF), múltiplos relacionamentos ManyToOne/OneToMany
5. **Flyway migrations**: Controle de versão de banco de dados
6. **Perfis de ambiente**: Dev, Test, Prod com configurações específicas
7. **Emails assíncronos**: Usando @Async do Spring com ServiceSendEmail
8. **Scheduled tasks**: Verificação de senhas vencidas a cada dia 11h

## Pontos a Melhorar

1. **Spring Boot Versão**: 4.0.6 é uma versão muito recente (lançada em 2026). Verificar compatibilidade de todas as dependências.
2. **Consulta CNPJ/CEP**: Uso de webservices externos (receitaws.com.br, viacep.com.br) que podem ficar indisponíveis
3. **Senhas em texto plano temporário**: O sistema gera senhas baseadas em timestamp (`Calendar.getInstance().getTimeInMillis()`) e as envia por email em texto plano
4. **Injeção de dependências circulares**: Complexidade no serviço PessoaUsuarioService com entityManager, jdbcTemplate, etc.
5. **Upload de arquivos**: Configuração de multipart em 10MB, mas sem serviço de armazenamento definido
6. **Tratamento de exceções**: Classe `ExceptionMentoriaJava` usada mas não vista o conteúdo completo
7. **CORS**: Configurado via headers em cada resposta JWT, mas poderia ser centralizado no WebConfig

## Observações Importantes

### Segurança JWT
- Token válido por 15 dias (`15L * 24 * 60 * 60 * 1000`)
- Segredo hardcoded: `"minha_palavra_secreta_super_segura_com_mais_de_32_caracteres"`
- O filtro `JwtApiAuthenticacaoFilter` tem uma exceção para rota `/salvarPj` (cadastro público)
- Ordem dos filtros é crítica: JWT primeiro, depois LoginFilter

### Herança JPA
- `Pessoa` é classe abstrata com herança `JOINED`
- `PessoaFisica` e `PessoaJuridica` usam `@PrimaryKeyJoinColumn`
- Isso causa complexidade em joins e deve ser cuidadosamente gerenciado

### Padrões de Design Observados
1. **Service Layer**: Busines logic centralizada
2. **Repository Pattern**: Acesso abstrato ao banco
3. **DTO Pattern**: Transferência de dados entre camadas
4. **Template Method**: Em TarefaAutomatizadasService (loop sobre usuários)
5. **Strategy Pattern**: ValidaCPF/ValidaCNPJ como classes utilitárias estáticas

### Problemas Potenciais
1. **Seqência de IDs**: O V2__ migration dropa uma constraint `uk8bak9jswon2id2jbunuqlfl9e` - pode indicar problemas anteriores de migration
2. **`spring.jpa.hibernate.ddl-auto=update`**: Em produção pode causar problemas; melhor usar `validate` ou migrações explícitas
3. **Criptografia de senha**: Usando BCryptPasswordEncoder corretamente
4. **Conexões HikariCP**: Configurações agressivas (max-pool-size=550, min-idle=200) - adequado para alta carga

## Testes

Três classes de teste principais:
- `LojaVirtualBackEndApplicationTests` - Tests com profile "test", testa endpoints REST de Acesso
- `TesteCPFeCNPJ` - Testa validação de CPF e CNPJ
- `TestePessoaUsuario` - Testa cadastro de PJ e PF através do controller

## Conclusão

O projeto Loja Virtual BackEnd é um sistema backend maduro e bem estruturado que resolve problemas complexos de cadastro de pessoas (físicas/jurídicas), validação de documentos, segurança JWT e operações comerciais completas (vendas, notas fiscais, estoque). A arquitetura demonstra bom conhecimento do ecossistema Spring Boot e boas práticas de desenvolvimento Java.

Os principais pontos de atenção são a versão recente do Spring Boot 4.x (garantir compatibilidade), o segredo hardcoded do JWT e o gerenciamento de senhas temporárias. O sistema está pronto para produção com devida configuração de ambiente e segredos externos.