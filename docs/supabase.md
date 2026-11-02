# Supabase PostgreSQL e Storage

Integração validada em 02/11/2026 com PostgreSQL 17.11, conexão SSL pelo Session Pooler, migrações Flyway e validação do esquema pelo Hibernate.

## Configuração do banco

1. Crie um projeto no Supabase e guarde a senha do PostgreSQL.
2. Em **Connect → Direct → Session pooler**, copie o host, porta e usuário informados pelo painel.
3. Defina as variáveis de ambiente do back-end:

```powershell
$env:SPRING_PROFILES_ACTIVE = 'supabase'
$env:DB_URL = 'jdbc:postgresql://HOST_DO_POOLER:5432/postgres?sslmode=require'
$env:DB_USER = 'postgres.ID_DO_PROJETO'
$env:DB_PASSWORD = 'SENHA_DO_BANCO'
```

4. Para importar o catálogo inicial em um banco novo, defina `CATALOG_SEED=true`.
5. Em um banco novo, `ADMIN_PASSWORD` permite criar o administrador no primeiro início. A senha dessa variável deve ter pelo menos oito caracteres. Usuários já existentes são preservados.
6. Execute o back-end. O Flyway aplica as migrações pendentes e o Hibernate valida o esquema.

As variáveis são lidas do ambiente do processo; arquivos `.env` não são carregados automaticamente pelo Spring. Senhas e chaves não devem ser versionadas nem incluídas no React.

## Migrações

| Versão | Conteúdo |
| --- | --- |
| V1 | Usuários, clientes, empregos, automóveis, pedidos, contratos e restrições |
| V2 | Metadados e categoria das imagens do catálogo |
| V3 | Atualização das URLs das imagens do catálogo para o Storage |

A V3 preserva URLs personalizadas que não correspondam à origem do catálogo inicial. Em um banco novo, os dados são importados após a criação do esquema.

## Storage

As 24 imagens do catálogo ficam no bucket público `veiculos`, com nomes `loc-001.jpg` a `loc-024.jpg`. Os endereços de leitura ficam no cadastro dos automóveis e em `catalogo.json`.

O acesso público do bucket permite visualizar as imagens. Envio, atualização e administração exigem uma chave secreta, utilizada somente no processo administrativo de migração. O navegador recebe apenas os endereços de leitura, sem a chave do projeto.

Os arquivos enviados foram verificados por SHA-256 após o upload. As 24 imagens foram carregadas novamente durante a validação no navegador.

## Acesso aos dados

A aplicação acessa o PostgreSQL via JDBC no servidor Spring. O React utiliza a API da aplicação, que aplica autenticação por sessão e permissões por cliente e papel. A Data API do Supabase não é necessária para esse fluxo.

Se a Data API for habilitada futuramente, configure permissões e RLS antes de disponibilizar as tabelas por ela. Não distribua credenciais de PostgreSQL ou chaves secretas aos usuários.

## Validação executada

- Migrações V1–V3 aplicadas no banco real e esquema validado.
- 34 verificações HTTP de autenticação, permissões, cadastros, pedidos, contratos e crédito no Supabase PostgreSQL.
- 11 verificações de interface no Chrome com persistência real, incluindo desktop, celular e as 24 imagens.
- Dados temporários das verificações removidos, preservando o catálogo e o administrador.

## Referências

- [Conexões PostgreSQL](https://supabase.com/docs/guides/database/connecting-to-postgres)
- [Supabase Storage](https://supabase.com/docs/guides/storage)
- [Chaves de API](https://supabase.com/docs/guides/api/api-keys)
