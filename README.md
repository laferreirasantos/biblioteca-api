📖 Biblioteca API - Sistema de Gestão de Empréstimo de Livros

API RESTful para gerenciamento de empréstimos de livros da biblioteca interna da PGE, baseada nos princípios de DDD, com Java 21, Spring Boot 3.5.0, PostgreSQL e Swagger UI.

✅ Funcionalidades

Cadastro e consulta de livros e usuários (funcionários)

Empréstimo de um ou mais livros para um usuário

Validações de regras de negócio:

Limite de 5 empréstimos por usuário

Empréstimos só são possíveis se houver exemplares disponíveis

Data de devolução máxima de 14 dias

Devolução de livros com atualização da quantidade

Consulta de livros disponíveis

Consulta de empréstimos por usuário

Verificação de usuários com livros em atraso

🌐 Tecnologias Utilizadas

Java 21

Spring Boot 3.5.0

Spring Data JPA

PostgreSQL

Docker (PostgreSQL)

Swagger UI (Springdoc OpenAPI 2.2.0)

JUnit 5 + Mockito

Maven

📁 Estrutura do Projeto (DDD)

com.pge.biblioteca
├── api (controllers + DTOs)
├── application (casos de uso)
├── domain
│   ├── model
│   ├── service
│   └── factory
├── infrastructure
│   └── persistence
├── shared (exceptions)
└── resources

⚙️ Como Executar Localmente (Passo a Passo)

1. Clonar o Repositório

git clone https://github.com/seu-usuario/biblioteca-api.git
cd biblioteca-api

2. Subir o Banco de Dados com Docker

Certifique-se de que o Docker esteja instalado e rodando.

docker run --name postgres-biblioteca \
  -e POSTGRES_DB=biblioteca \
  -e POSTGRES_USER=postgresql \
  -e POSTGRES_PASSWORD=postgresql \
  -p 5432:5432 \
  -d postgres:15

3. Configuração do application.yml

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/biblioteca
    username: postgresql
    password: postgresql
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  jackson:
    serialization:
      indent_output: true

server:
  port: 8080

springdoc:
  swagger-ui:
    path: /swagger-ui.html

4. Importar o Projeto na IDE (Eclipse)

File > Import > Existing Maven Projects

Selecione o diretório clonado

5. Rodar o Projeto

Localize a classe BibliotecaApiApplication.java

Clique com o botão direito e selecione Run As > Java Application

6. Acessar Swagger UI

Acesse:

http://localhost:8080/swagger-ui.html

Lá você pode testar todos os endpoints de forma interativa.

🔧 Testes

Execute os testes com:

mvn test

Inclui testes unitários para:

Domain Services (EmprestimoService, LivroService, UsuarioService)

Controllers

🌟 Autor

Desenvolvido por Larissa, desenvolvedora backend com foco em Java, Spring Boot, PostgreSQL e APIs REST.

GitHub | LinkedIn

✨ Sugestões de Melhorias Futuras

Autenticação com JWT (Spring Security)

Interface frontend com Angular ou React

Deploy em nuvem (Render, Railway ou AWS)

