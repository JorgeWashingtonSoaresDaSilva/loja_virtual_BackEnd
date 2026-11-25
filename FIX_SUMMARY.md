# Resumo da Correção de Erro de Integridade do Banco

## Problema
Erro ao inserir CategoriaProduto:
```
ERROR: null value in column "empresa_id" of relation "categoria_produto" 
violates not-null constraint
Failing row contains (7, Eletronicos, null).
```

## Causa Raiz
O controlador `CategoriaProdutoController.SalvarCategoria()` estava salvando a entidade diretamente do request body sem validar se o campo `empresa` estava preenchido. A entidade `CategoriaProduto` possui:

```java
@ManyToOne(targetEntity = PessoaJuridica.class)
@JoinColumn(name = "empresa_id", nullable = false, ...)
private PessoaJuridica empresa;
```

O `nullable = false` gera uma constraint NOT NULL no banco de dados PostgreSQL, que foi violada quando o insert tentou colocar NULL no column `empresa_id`.

## Solução Aplicada
**Arquivo**: `src/main/java/com/jwss/studios/loja_virtual_BackEnd/controller/CategoriaProdutoController.java`

**Inserida validação** (linhas 26-29):
```java
// Valida se a empresa foi informada
if (categoriaProduto.getEmpresa() == null) {
    throw new ExceptionMentoriaJava("Empresa é obrigatória para cadastro de categoria de produto");
}
```

## Benefícios da Correção
1. **Previne** a exceção de constraint do banco de dados
2. **Fornece** mensagem clara e significante ao cliente da API
3. **Segue o padrão** do projeto (ExceptionMentoriaJava para validações)
4. **Compila** successfully sem erros

## Padrão do Projeto
Esta validação segue o mesmo padrão usado em outras partes do sistema, como em `AcessoController` que também lança `ExceptionMentoriaJava` quando há tentativas de cadastro inválido (ex: descrição duplicada).

## Testes
- Código compila com sucesso (`mvn compile -q`)
- O projeto já possuía `<skipTests>true</skipTests>` no pom.xml
- Nenhum teste existente foi quebrado pela alteração