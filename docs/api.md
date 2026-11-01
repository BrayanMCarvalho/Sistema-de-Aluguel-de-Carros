# API e permissões

Base `/api`, no mesmo domínio do front-end. Sessão HTTP em cookie HttpOnly. Não usa token salvo em localStorage.

| Método | Rota | Acesso |
| --- | --- | --- |
| GET | `/auth/csrf` | Público, devolve token e nome do cabeçalho |
| POST | `/auth/register` | Público, JSON de cadastro de cliente |
| POST | `/auth/login` | Público, formulário `username` / `password` |
| POST | `/auth/logout` | Sessão, invalida login |
| GET | `/auth/me` | Sessão, usuário atual sem senha |
| GET | `/automoveis` | Qualquer usuário autenticado |
| GET | `/bancos` | Qualquer usuário autenticado |
| GET | `/clientes` | Cliente: próprio; agentes/admin: todos |
| GET, PUT, DELETE | `/clientes/{id}` | Cliente: próprio; agentes/admin: cadastro indicado |
| GET | `/pedidos` | Cliente: próprios; agentes/admin: todos |
| GET, PUT | `/pedidos/{id}` | Cliente: próprio; agentes/admin: indicado |
| POST | `/pedidos` | Cliente autenticado |
| POST | `/pedidos/{id}/cancelar` | Cliente dono ou agente/admin |
| POST | `/pedidos/{id}/avaliar` | Empresa, banco ou admin |
| POST | `/pedidos/{id}/contrato` | Empresa, banco ou admin; crédito só banco |
| GET | `/contratos` | Cliente: próprios; agentes/admin: todos |
| GET | `/admin/usuarios` | Admin |
| POST | `/admin/agentes` | Admin |

Antes de escrever, consulte `/auth/csrf` e envie o cabeçalho indicado na resposta. Após login/logout, obtenha um token novo. O cliente web já implementa isso em `frontend/src/api.js`.

## Exemplos de corpos JSON

Pedido:

```json
{
  "automovelId": 1,
  "inicio": "2027-01-10",
  "fim": "2027-01-13",
  "observacoes": "Viagem em família"
}
```

Avaliação:

```json
{ "aprovado": true, "parecer": "Renda compatível com a contratação." }
```

Contrato sem crédito:

```json
{
  "proprietarioTipo": "CLIENTE",
  "proprietarioNome": "Nome do contratante",
  "valorCredito": null,
  "parcelas": null
}
```

Banco autenticado pode informar crédito positivo e número de parcelas no mesmo corpo. O servidor registra o próprio banco como concedente; o solicitante não escolhe outro ID de banco.

## Respostas

`200` consulta/alteração, `201` criação, `204` exclusão/logout, `400` regra de negócio ou formato inválido, `401` sem login, `403` sem permissão/CSRF, `404` não encontrado e `409` duplicidade ou concorrência. Erros de negócio retornam `{"message":"descrição"}`. Erros de segurança podem retornar o formato padrão do Spring.
