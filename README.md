# Sistema de Aluguel de Carros

Projeto do Laboratório 02 de Desenvolvimento e Integração de Aplicações Web, do curso de Engenharia de Software da PUC Minas.

**Professor:** João Paulo Carneiro Aramuni
**Aluno:** Brayan Mendes de Carvalho

## Sobre o projeto

O sistema permite que clientes cadastrem, consultem, alterem e cancelem pedidos de aluguel de automóveis. Empresas e bancos atuam como agentes responsáveis pela avaliação financeira dos pedidos e execução dos contratos aprovados.

## Status

Já foram publicados a modelagem, as entidades, os repositórios, as migrações SQL, os serviços de negócio, a configuração de segurança e os perfis de banco. Esta etapa inclui dados de demonstração e o catálogo de 24 veículos. Os controllers da API e a interface React serão publicados nas próximas etapas.

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

## Domínio e persistência

- Entidades: usuário, cliente, automóvel, pedido e contrato.
- Clientes com até três empregos e respectivos rendimentos.
- Papéis de cliente, empresa, banco e administrador.
- Repositórios Spring Data JPA com consultas por cliente e controle de concorrência nos pedidos.
- Migrações Flyway para tabelas, relacionamentos, restrições e metadados das fotos.

## Serviços e configuração

- Regras de cadastro de clientes, pedidos, avaliação financeira e emissão de contratos.
- Controle de acesso por cliente, empresa, banco e administrador.
- Proteção de rotas, autenticação por sessão, senhas BCrypt e CSRF.
- Perfis de execução: demonstração local com H2 e Supabase PostgreSQL por variáveis de ambiente.
- Contas de demonstração e catálogo inicial de 24 veículos com fotos e créditos.

O perfil Supabase está preparado, mas a conexão real ainda depende das credenciais do ambiente. Veja [configuração do Supabase](docs/supabase.md).

## Instalação e execução

Pré-requisitos desta etapa: JDK 25 e Apache Maven 3.9 ou superior.

Para compilar a estrutura inicial do back-end:

    cd backend
    mvn compile

O back-end publicado inclui os serviços e as configurações de execução. Os controllers da API e o front-end ainda não fazem parte desta etapa; o fluxo completo pelo navegador será disponibilizado com sua publicação.

## Autor

[Brayan Mendes de Carvalho](https://github.com/BrayanMCarvalho)

## Referência

README organizado a partir do [template disponibilizado pelo professor](https://github.com/joaopauloaramuni/desenvolvimento-e-integracao-de-aplicacoes-web/blob/main/TEMPLATES/template_README.md), adaptado à etapa atual do repositório.
