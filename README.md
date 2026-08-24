# Loja Virtual - BackEnd

Este é o projeto de BackEnd para uma **Loja Virtual**, desenvolvido com foco em alta performance, segurança e escalabilidade.

## 🚀 Tecnologias Utilizadas

O projeto utiliza o ecossistema moderno do Java e do framework Spring:

* **Java 21:** Últimas funcionalidades e alta performance da linguagem.
* **Spring Boot 4.1.1:** Framework base para agilizar o desenvolvimento.
* **Spring Data JPA:** Abstração de banco de dados com **Hibernate** integrado.
* **Spring Data REST:** Exposição automática de repositórios como endpoints REST.
* **Spring Security:** Proteção de rotas, autenticação e autorização.
* **Spring Web MVC:** Arquitetura para construção de APIs RESTful.
* **PostgreSQL:** Banco de dados relacional robusto para produção.
* **Spring Boot DevTools:** Reinício automático do servidor durante o desenvolvimento.
* **Maven:** Gerenciador de dependências e automação do build.

## 🧪 Tecnologias de Testes

O projeto está estruturado com testes automatizados para cada camada:

* **Data JPA Test:** Validação de repositórios e consultas ao banco.
* **Data REST Test:** Testes dos endpoints HTTP auto-gerados.
* **Security Test:** Validação das regras de acesso e autenticação.
* **WebMVC Test:** Testes de controladores e fluxos da web.

## 🛠️ Como Executar o Projeto

1. Certifique-se de ter o **Java 21** e o **Maven** instalados.
2. Configure as credenciais do seu banco **PostgreSQL** no arquivo `src/main/resources/application.properties`.
3. Execute o comando para iniciar a aplicação:
   ```bash
   mvn spring-boot:run
   ```
