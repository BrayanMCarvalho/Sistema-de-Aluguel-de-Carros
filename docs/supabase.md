# Conectar o Supabase

1. Crie um projeto PostgreSQL e anote a senha do banco.
2. Em **Connect**, copie o host, porta, nome do banco e usuário da conexão JDBC, preferindo conexão direta ou Session Pooler. Algumas redes não oferecem IPv6 para conexão direta.
3. Defina as variáveis `SPRING_PROFILES_ACTIVE=supabase`, `DB_URL`, `DB_USER` e `DB_PASSWORD` no terminal onde iniciará o projeto.
4. Acrescente `?sslmode=require` à URL JDBC e use o usuário exato informado pelo Supabase; no pooler ele pode conter o identificador do projeto.
5. Defina `ADMIN_PASSWORD` para criar o administrador e, se desejar, `CATALOG_SEED=true` para importar os 24 veículos de demonstração.
6. Inicie a API. O Flyway aplica `V1` e `V2`; o Hibernate valida a compatibilidade das tabelas.
7. Teste login, cadastro, pedido e consulta no SQL Editor antes da apresentação.

As tabelas não devem ficar expostas à API pública do Supabase. Esta aplicação acessa PostgreSQL somente no servidor via JDBC; o front-end conversa com a API Spring. Não envie a senha PostgreSQL nem uma chave `service_role` para o React.

Se o projeto Supabase mantiver o esquema `public` acessível via Data API, use o SQL abaixo para bloquear o acesso anônimo e autenticado direto às tabelas da aplicação. A conexão JDBC deve usar o proprietário do esquema ou uma role de servidor com os privilégios necessários. Configure um banco dedicado ao trabalho.

```sql
REVOKE ALL ON TABLE public.usuarios, public.clientes, public.empregos,
  public.automoveis, public.pedidos, public.contratos, public.flyway_schema_history
  FROM anon, authenticated;
ALTER TABLE public.usuarios ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.clientes ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.empregos ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.automoveis ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.pedidos ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.contratos ENABLE ROW LEVEL SECURITY;
```

Não crie políticas públicas para contornar o login da aplicação. A autorização por cliente e papel é aplicada pelo serviço Spring. A conta PostgreSQL usada pelo back-end não deve ser distribuída aos usuários.

## Importante para a avaliação

O perfil `demo` usa H2 e não comprova compatibilidade executada com o Supabase. As migrações usam SQL compatível com PostgreSQL, mas o teste na instância real só poderá ser concluído após configurar as credenciais. Guarde evidências dessa conexão na apresentação.
