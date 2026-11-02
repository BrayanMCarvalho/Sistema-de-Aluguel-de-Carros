# Publicação no Render

O Dockerfile compila a interface React e a inclui no JAR do Spring Boot. Um único Web Service publica a interface e a API no mesmo domínio; banco e imagens continuam no Supabase.

## Criar o serviço

No Render, selecione **New → Web Service → Public Git Repository** e informe:

`https://github.com/BrayanMCarvalho/Sistema-de-Aluguel-de-Carros`

Esse fluxo não exige conectar a conta GitHub ao Render.

| Campo | Valor |
| --- | --- |
| Branch | `main` |
| Language | `Docker` |
| Root Directory | Vazio, usando a raiz do repositório |
| Dockerfile Path | `./Dockerfile` |
| Instance Type | `Free` para o protótipo acadêmico |
| Health Check Path | `/api/health` |

## Variáveis de ambiente

| Nome | Valor |
| --- | --- |
| `SPRING_PROFILES_ACTIVE` | `supabase` |
| `DB_URL` | URL JDBC do Session Pooler com `?sslmode=require` |
| `DB_USER` | Usuário PostgreSQL informado pelo Supabase |
| `DB_PASSWORD` | Senha do PostgreSQL, preenchida apenas no painel do Render |
| `COOKIE_SECURE` | `true` |
| `APP_DEMO` | `true` para disponibilizar as contas de demonstração do trabalho |

`APP_DEMO=true` cria as contas ausentes sem substituir as existentes; a conexão continua no Supabase porque o perfil ativo é `supabase`. O banco atual já contém o catálogo. Para um banco novo, pode-se definir também `CATALOG_SEED=true`.

Não envie a chave secreta do Storage ao Render: a aplicação só precisa dos endereços públicos das imagens para exibi-las.

## Execução

O Render fornece a variável `PORT`, aceita pelo Spring. A interface usa `/api` no próprio domínio, preservando sessão e proteção CSRF sem um serviço separado de front-end.

No plano gratuito, o serviço entra em repouso após inatividade; a primeira abertura pode demorar enquanto ele reinicia. Os dados e imagens ficam no Supabase e são preservados.

## Atualizações

Ao usar o link público, publique atualizações por **Manual Deploy → Deploy latest commit**. A conexão opcional por Git Provider permite habilitar deploy automático, mas não é necessária para hospedar este projeto.
