# Análise do Erro de Integridade do Banco

## Contexto
Erro encontrado durante operação de inserção de `CategoriaProduto` no banco de dados PostgreSQL.

## Mensagem de Erro
```
could not execute batch [Batch entry 0 insert into categoria_produto (empresa_id,nome_desc,id) values ((NULL),('Eletronicos'),('7'::int8)) was aborted: 
ERROR: null value in column "empresa_id" of relation "categoria_produto" violates not-null constraint
Detalhe: Failing row contains (7, Eletronicos, null).
```

## Raiz do Problema

### Entidade `CategoriaProduto`
```java
@Entity
@Table(name = "categoria_produto")
public class CategoriaProduto implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String nomeDesc;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, 
                foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
    private PessoaJuridica empresa;  // <--- NOT NULL no banco
}
```

### Controlador Problemático
```java
@PostMapping(value = "**/salvarCategoria")
public ResponseEntity<CategoriaProdutoDTO> SalvarCategoria(@RequestBody CategoriaProduto categoriaProduto) {
    // Nenhuma validação de empresa!
    CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);
    // ... tenta acessar categoriaSalva.getEmpresa().getId()
}
```

### Fluxo do Erro
1. Requisição HTTP POST com corpo JSON contendo `{ "nomeDesc": "Eletronicos" }` (sem empresa)
2. Controller chama `categoriaProdutoRepository.save(categoriaProduto)`
3. Hibernate gera INSERT: `INSERT INTO categoria_produto (empresa_id, nome_desc, id) VALUES (NULL, 'Eletronicos', 7)`
4. Banco de dados rejeita: constraint NOT NULL violada na coluna `empresa_id`
5. Exceção lançada para o cliente

## Padrão de Outras Entidades
Outras entidades do mesmo projeto também possuem `empresa_id` NOT NULL:
- `Produto.empresa` (ManyToOne Pessoa)
- `MarcaProduto.empresa` (ManyToOne Pessoa)
- `FormaPagamento.empresa` (ManyToOne Pessoa)
- `CupomDesconto.empresa` (ManyToOne Pessoa)
- `NotaFiscalCompra.empresa` (ManyToOne Pessoa)
- `NotaFiscalVenda.empresa` (ManyToOne Pessoa)
- `VendaCompraLojaVirtual.empresa` (ManyToOne Pessoa)
- `StatusRastreio.empresa` (ManyToOne Pessoa)
- `AvaliacaoProduto.empresa` (ManyToOne Pessoa)
- `ItemVendaLoja.empresa` (ManyToOne Pessoa)
- `Endereco.empresa` (ManyToOne Pessoa)

## Solução Implementada
Adicionada validação no controlador para verificar se `empresa` está presente antes de salvar:

```java
if (categoriaProduto.getEmpresa() == null) {
    throw new ExceptionMentoriaJava("Empresa é obrigatória para cadastro de categoria de produto");
}
```

Isso:
- ✅ Impede o INSERT com NULL no banco
- ✅ Retorna erro controlado ao cliente API
- ✅ Segue convenção do projeto
- ✅ Facita debugging e tratamento de erros

## Arquivos Modificados
- `src/main/java/com/jwss/studios/loja_virtual_BackEnd/controller/CategoriaProdutoController.java` - Adicionada validação (linhas 26-29)

## Como Testar
### Requisição inválida (sem empresa):
```bash
curl -X POST http://localhost:8080/loja_virtual_mentoria/salvarCategoria
Content-Type: application/json

{ "nomeDesc": "Eletronicos" }
```

### Resultado Esperado
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Empresa é obrigatória para cadastro de categoria de produto"
}
```

### Requisição válida (com empresa):
```bash
curl -X POST http://localhost:8080/loja_virtual_mentoria/salvarCategoria
Content-Type: application/json

{
  "nomeDesc": "Eletronicos",
  "empresa": { "id": 1 }
}
```

### Resultado Esperado
```json
{
  "id": 8,
  "nomeDesc": "Eletronicos",
  "empresa": "1"
}
```