# Modelagem UML v1

- [Casos de uso](casos-de-uso.puml)
- [Classes](classes.puml)
- [Pacotes / visão lógica](pacotes.puml)
- [Histórias do usuário e regras](../../requisitos.md)

Arquivos de origem em PlantUML, editáveis e versionáveis. Abra em um visualizador PlantUML ou use um renderizador local; nenhuma extensão foi instalada automaticamente.

`Usuario.papel` diferencia cliente, empresa, banco e administrador. O administrador é uma decisão do protótipo solicitada pelo usuário; não substitui o agente bancário na concessão de crédito. Relações rotuladas como `ID` são implementadas por chaves estrangeiras, sem associação JPA direta. `Pedido.automovel` é uma associação `@ManyToOne`.

A implementação não cria uma classe separada para contrato de crédito: seus atributos opcionais ficam em `Contrato`, com banco concedente obrigatório quando há crédito. A decisão é registrada para manter o modelo alinhado ao código.
