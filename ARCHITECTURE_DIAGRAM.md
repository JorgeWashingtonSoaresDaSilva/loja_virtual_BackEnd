# Diagrama de Arquitetura do Loja Virtual BackEnd

```mermaid
%%{init: {'theme': 'base', 'themeVariables': { 'primaryColor': '#1a365d', 'primaryColorSecondary': '#2d5a87', 'primaryAccentColor': '#4a90d9', 'secondaryColor': '#f0f4f8', 'tertiaryColor': '#ffffff', 'fontFamily': 'Helvetica, Arial, sans-serif' }}}%%

graph TD
    %% Styles
    classDef layer fill:#f8f9fa,stroke:#1a365d,stroke-width:2px,color:#000000,font-weight:bold,rx:5,ry:5]
    classDef component fill:#e9ecef,stroke:#6c757d,stroke-width:1px,color:#000000,rx:3,ry:3]
    classDef entity fill:#dde8f0,stroke:#4a90d9,stroke-width:1.5px,color:#000000,rx:3,ry:3]
    classDef service fill:#d1d9e1,stroke:#2c3e50,stroke-width:1px,color:#000000,rx:3,ry:3]
    classDef controller fill:#a8d5e2,stroke:#005f73,stroke-width:1.5px,color:#000000,rx:3,ry:3]
    classDef external fill:#fff3e0,stroke:#f39c12,stroke-width:1.5px,color:#000000,rx:3,ry:3]
    classDef security fill:#fadbd8,reda:#e87979,stroke-width:1.5px,rx:3,ry:3]

    %% ============================================================
    %% CAMADA DE CONTROLADORES (Presentation Layer)
    %% ============================================================
    subgraph CONTROLLERS [🎛️ CAMADA DE CONTROLADORES]
        direction TB
        A1[AcessoController]:::controller
        P1[PessoaController]:::controller
    end

    %% ============================================================
    %% CAMADA DE SERVIÇOS (Business Logic Layer)
    %% ============================================================
    subgraph SERVICES [🔧 CAMADA DE SERVIÇOS]
        direction TB
        S1[PessoaUsuarioService]:::service
        S2[AcessoService]:::service
        S3[TarefaAutomatizadasService]:::service
        S4[ServiceSendEmail]:::service
    end

    %% ============================================================
    %% CAMADA DE REPOSITÓRIOS (Data Access Layer)
    %% ============================================================
    subgraph REPOSITORIES [💾 CAMADA DE REPOSITÓRIOS]
        direction TB
        R1[PessoaRepository]:::component
        R2[UsuarioRepository]:::component
        R3[AcessoRepository]:::component
        R4[EnderecoRepository]:::component
        R5[PessoaFisicaRepository]:::component
    end

    %% ============================================================
    %% ENTIDADES DO DOM (Domain Layer)
    %% ============================================================
    subgraph ENTITIES [📦 ENTIDADES DO DOM]
        direction TB
        %% Pessoa hierarchy
        P[Pessoa]:::entity
        PF[PessoaFisica]:::entity
        PJ[PessoaJuridica]:::entity
        
        %% Core entities
        U[Usuario]:::entity
        A[Acesso]:::entity
        E[Endereco]:::entity
        PR[Produto]:::entity
        CP[CategoriaProduto]:::entity
        MP[MarcaProduto]:::entity
        FP[FormaPagamento]:::entity
        CD[CupomDesconto]:::entity
        NP[NotaFiscalCompra]:::entity
        NV[NotaFiscalVenda]:::entity
        VC[VendaCompraLojaVirtual]:::entity
        IV[ItemVendaLoja]:::entity
        CPG[ContaPagar]:::entity
        CR[ContaReceber]:::entity
        SR[StatusRastreio]:::entity
        IP[ImagemProduto]:::entity
        AP[AvaliacaoProduto]:::entity
        
        %% Relations
        P -->|herança| PF
        P -->|herança| PJ
    end

    %% ============================================================
    %% RELATIONSHIPS BETWEEN ENTITIES
    %% ============================================================

    %% Pessoa relations
    U --* A (acessos):::entity
    U -->|fk pessoa| P
    U -->|fk empresa| P
    P -->|1:m enderecos| E
    P -->|1:m produtos| PR
    PF -->|1:1 pessoa_fisica| PF
    PJ -->|1:1 pessoa_juridica| PJ

    %% Product relations
    PR -->|m:1 categoria| CP
    PR -->|m:1 marca| MP
    PR -->|1:m avaliacoes| AP
    PR -->|1:m imagens| IP
    PR -->|1:m itens_venda| IV
    PR -->|1:m notas_fiscal_compra| NP

    %% Sale relations
    VC -->|1:m itens| IV
    VC -->|1:1 nota_fiscal_venda| NV
    VC -->|1:m contas_pagar| CPG
    VC -->|1:m contas_receber| CR
    VC -->|1: forma_pagamento| FP
    VC -->|1: cupom_desconto| CD
    IV -->|fk produto| PR
    NV -->|fk venda_compra| VC

    %% Financial relations
    CPG -->|fk pessoa| P
    CPG -->|fk pessoa_fornecedor| P
    CR -->|fk pessoa| P
    CR -->|fk empresa| P
    SR -->|fk venda_compra| VC

    %% Enums relationships
    CPG -->|enum status| StatusContaPagar
    CR -->|enum status| StatusContaReceber
    E -->|enum tipo| TipoEndereco

    %% ============================================================
    %% SERVICE LAYER CONNECTIONS
    %% ============================================================

    S1 -->|usa| R1
    S1 -->|usa| R2
    S1 -->|usa| R5
    S1 -->|usa| U
    S1 -->|usa| P
    S1 -->|usa| E
    S1 -->|usa| FP
    S1 -->|usa| CD
    S1 -->|usa| VC
    S1 -->|usa| NV
    S1 -->|usa| CPG
    S1 -->|usa| CR
    S1 -->|usa| SR

    S2 -->|usa| R3
    S2 -->|usa| A

    S3 -->|usa| R2
    S3 -->|usa| S4

    S4 -->|envia email| external(✉️ Gmail SMTP)
    S4 -->|usa config| external(application.properties)

    %% ============================================================
    %% CONTROLLER LAYER CONNECTIONS
    %% ============================================================
    A1 -->|chama| S2
    A1 -->|responde HTTP| external(frontend/client)

    P1 -->|chama| S1
    P1 -->|consulta CEP| external(⚡ ViaCEP API)
    P1 -->|consulta CNPJ| external(📊 ReceitaWS API)

    %% ============================================================
    %% DATABASE LAYER
    %% ============================================================
    subgraph DATABASE [🗄️ BANCO DE DADOS PostgreSQL]
        direction TB
        DB1[(23 Tabelas)]
        DB2[(27 Sequências)]
        DB3[(Migrações Flyway)]
    end

    REPOSITORIES -->|JPA/Hibernate| DATABASE
    S1 -->|transações| DATABASE
    S2 -->|transações| DATABASE
    S3 -->|transações| DATABASE

    %% ============================================================
    %% SECURITY LAYER
    %% ============================================================
    subgraph SECURITY [🔒 CAMADA DE SEGURANÇA]
        direction TB
        SEC1[WebConfigSecurity]:::security
        SEC2[JWTTokenAutenticacaoService]:::security
        SEC3[JWTLoginFilter]:::security
        SEC4[JwtApiAuthenticacaoFilter]:::security
    end

    SEC1 -->|configura| SEC2
    SEC1 -->|configura| SEC3
    SEC1 -->|configura| SEC4

    SEC2 -->|gera/valida token| external(HTTP Headers)
    SEC3 -->|processa login POST /login| external(HTTP Request)
    SEC4 -->|filtra APIs| external(HTTP Requests)

    %% ============================================================
    %% EXTERNAL SERVICES
    %% ============================================================
    external CEP_API[⚡ ViaCEP API]
    external CNPJ_API[📊 ReceitaWS API]
    external MAIL_SMTP[📧 Gmail SMTP]

    P1 -->|requisição| CEP_API
    P1 -->|requisição| CNPJ_API
    S4 -->|enviar email| MAIL_SMTP

    %% ============================================================
    %% NOTAS RODAPE
    %% ============================================================
    class "Largura de 15 dias":::security[Token JWT: validade 15 dias]
    class "BCryptPasswordEncoder":::security[Criptografia de senha]
    class "Flyway V1/V2":::database[Migrações de BD]
    class "Perfis: dev/test/prod":::external[spring.profiles.active]
```

## Visão Geral do Diagrama

O diagrama acima ilustra a arquitetura em camadas do **Loja Virtual BackEnd** com:

### 🏗️ **Camadas Principais**
1. **Controladores** - AcessoController, PessoaController (entradas/saídas HTTP)
2. **Serviços** - Business logic centralizada (PessoaUsuarioService, AcessoService, etc.)
3. **Repositórios** - Acesso ao banco de dados (JPA/Hibernate)
4. **Entidades** - Modelos do domínio (26 classes principais)
5. **Banco de Dados** - PostgreSQL com 23 tabelas e 27 sequências

### 🔒 **Segurança**
- Spring Security 7 com SecurityFilterChain configurado
- JWT Token com 15 dias de validade
- Filtros: JWTLoginFilter → JwtApiAuthenticacaoFilter
- Rotas públicas marcadas explicitamente

### 🌐 **Serviços Externos**
- **ViaCEP** - Consulta de endereços por CEP
- **ReceitaWS** - Consulta de dados CNPJ
- **Gmail SMTP** - Envio de emails assíncronos

### ⏰ **Tarefas Agendadas**
- TarefaAutomatizadasService - Verificação de senhas vencidas (cron diário 11h)

### 📊 **Relacionamentos Complexos**
- Herança JOAOIN: Pessoa → PessoaFisica/PessoaJuridica
- Muitos para muitos: Usuario ↔ Acesso (via tabela junction `usuarios_acesso`)
- Herança estratégica: `@Inheritance(strategy = InheritanceType.JOINED)`
- Empresa relacionamento em todas as entidades principais