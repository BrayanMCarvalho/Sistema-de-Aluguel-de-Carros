# Evolução da modelagem

## v1 — 27/10/2026

Primeira versão criada junto ao protótipo. Inclui clientes, empresas e bancos representados por papéis de usuário, contratos com crédito opcional e subsistema de apresentação React.

Decisões incorporadas: administrador para provisionar agentes; até três rendimentos; alterações reabrem análise; vínculo por ID preserva clientes, avaliadores e bancos; estado terminal para contrato e cancelamento; URL/autor/licença da foto no automóvel.

A versão v1 corresponde à estrutura do protótipo publicado. Futuras alterações de modelagem devem preservar esta versão e registrar os motivos e a relação com o código.

## Integração — 02/11/2026

Integração validada no Supabase PostgreSQL 17.11 e imagens do catálogo migradas para o Supabase Storage. A migração V3 atualiza as URLs do catálogo existente. Não houve alteração de entidades ou relacionamentos; os diagramas v1 continuam correspondendo ao código.
