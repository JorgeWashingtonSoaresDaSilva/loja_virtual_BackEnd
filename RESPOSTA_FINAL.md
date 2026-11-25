# Correção de Erro de Integridade do Banco - Loja Virtual BackEnd

## Problema Reportado
```
Erro de integridade do banco: could not execute batch [Batch entry 0 insert into categoria_produto (empresa_id,nome_desc,id) values ((NULL),('Eletronicos'),('7'::int8)) was aborted: ERROR: null value in column "empresa_id" of relation "categoria_produto" violates not-null constraint Detalhe: Failing row contains (7, Eletronicos, null).
```

## Causa Raiz
O controlador `CategoriaProdutoController.SalvarCategoria()` estava salvando a entidade diretamente do request body sem validar se o campo `empresa` estava preenchido. Como a entidade `CategoriaProduto` tem `@JoinColumn(name = "empresa_id", nullable = false)`, o banco de dados PostgreSQL rejeitava o insert quando `empresa_id` era NULL.

## Solução Aplicada

### Arquivo Modificado
`src/main/java/com/jwss/studios/loja_virtual_BackEnd/controller/CategoriaProdutoController.java`

### Código Adicionado (linhas 26-29)
```java
// Valida se a empresa foi informada
if (categoriaProduto.getEmpresa() == null) {
    throw new ExceptionMentoriaJava("Empresa é obrigatória para cadastro de categoria de produto");
}
```

## Como Funciona Agora

### Requisição Sem Empresa (Inválida)
```bash
curl -X POST http://localhost:8080/loja_virtual_mentoria/salvarCategoria
Content-Type: application/json

{ "nomeDesc": "Eletronicos" }
```

### Resposta Esperada
```json
{
  "timestamp": "2026-11-24T...",
  "status": 400,
  "error": "Bad Request",
  "message": "Empresa é obrigatória para cadastro de categoria de produto",
  "path": "/loja_virtual_mentoria/salvarCategoria"
}
```

### Requisição Com Empresa (Válida)
```bash
curl -X POST http://localhost:8080/loja_virtual_mentoria/salvarCategoria
Content-Type: application/json

{
  "nomeDesc": "Eletronicos",
  "empresa": { "id": 1 }
}
```

### Resposta Esperada
```json
{
  "id": 8,
  "nomeDesc": "Eletronicos",
  "empresa": "1"
}
```

## Arquivos Gerados Durante a Análise

1. **PROJECT_ANALYSIS.md** - Análise completa do projeto (estrutura, segurança, banco de dados)
2. **ARCHITECTURE_DIAGRAM.md** - Diagrama Mermaid da arquitetura em 5 camadas
3. **FIX_SUMMARY.md** - Resumo da correção aplicada
4. **ERROR_ANALYSIS.md** - Análise detalhada do erro e causa raiz

## Verificação
- ✅ Código compila com sucesso (`mvn compile -q`)
- ✅ Padrão consistente com validações existentes no projeto
- ✅ Mensagem de erro clara para desenvolvedores/cliente API
- ✅ Evita exceções brutais de constraint do banco de dados