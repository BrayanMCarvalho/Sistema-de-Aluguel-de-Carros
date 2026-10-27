# Sistema de Aluguel de Carros

Projeto do Laboratório 02 de Desenvolvimento e Integração de Aplicações Web, do curso de Engenharia de Software da PUC Minas.

**Professor:** João Paulo Carneiro Aramuni  
**Aluno:** Brayan Mendes de Carvalho

## Sobre o projeto

O sistema permite que clientes cadastrem, consultem, alterem e cancelem pedidos de aluguel de automóveis. Empresas e bancos atuam como agentes responsáveis pela avaliação financeira dos pedidos e execução dos contratos aprovados.

## Status

Repositório em organização. Esta primeira publicação reúne a especificação e a modelagem do sistema. O código da aplicação será incorporado em commits posteriores.

## Funcionalidades previstas no laboratório

- Cadastro e autenticação com usuário e senha.
- CRUD de clientes com RG, CPF, nome, endereço, profissão e até três entidades empregadoras e seus rendimentos.
- Registro de automóveis com matrícula, ano, marca, modelo e placa.
- Criação, consulta, alteração e cancelamento de pedidos de aluguel.
- Avaliação financeira por empresas e bancos.
- Contratos com propriedade de clientes, empresas ou bancos.
- Associação opcional de crédito concedido por um banco agente.

## Tecnologias

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java — JDK 25 |
| Back-end | Spring Boot 4.0.x, Spring MVC |
| Build | Apache Maven |
| Front-end | React + Vite |
| Banco de dados | Supabase — PostgreSQL |
| Autenticação | Spring Security |

## Arquitetura e modelagem

Arquitetura MVC, com subsistemas de apresentação web e gestão de pedidos e contratos. A interface React comunica-se com a API Spring; os dados são persistidos em PostgreSQL.

Documentação:

- [Histórias do usuário e regras](docs/requisitos.md)
- [Modelagem UML v1](docs/modelagem/v1/README.md)
- [Histórico da modelagem](docs/modelagem/CHANGELOG.md)

## Instalação e execução

As instruções de instalação acompanharão a publicação do código. Os pré-requisitos são JDK 25, Maven, Node.js e um projeto PostgreSQL no Supabase.

## Autor

[Brayan Mendes de Carvalho](https://github.com/BrayanMCarvalho)

## Referência

README organizado a partir do [template disponibilizado pelo professor](https://github.com/joaopauloaramuni/desenvolvimento-e-integracao-de-aplicacoes-web/blob/main/TEMPLATES/template_README.md), adaptado à etapa atual do repositório.
