# 🚀 Atividade A05 - Módulo de Faturamento ERP: Padrão Builder

## 🧠 Sobre o Processo de Desenvolvimento e Metodologia
Este projeto foi desenvolvido utilizando uma abordagem de **Pair Programming (Programação em Par) assistida por IA** (Gemini). 

O objetivo principal desta entrega não foi apenas apresentar um código funcional, mas sim compreender profundamente a aplicação prática do padrão criacional **Builder** na construção de objetos complexos e imutáveis. A inteligência artificial foi utilizada ativamente como uma ferramenta de tutoria técnica para auxiliar na transição dos conceitos teóricos para a implementação real em Java, atuando na estruturação do código, no entendimento das regras de visibilidade da linguagem e na refatoração do anti-padrão de construtores telescópicos.

As decisões arquiteturais comentadas neste documento e no código-fonte servem não apenas como justificativa para a atividade, mas como uma base de conhecimento pessoal para consultas futuras.

## 🏢 O Problema Arquitetural
No módulo de faturamento de um ERP, a entidade `NotaFiscal` exige diversos atributos (obrigatórios e opcionais). A abordagem inicial e ingênua resultaria no **Anti-padrão do Construtor Telescópico** (Telescoping Constructor), onde o desenvolvedor é forçado a passar múltiplos valores nulos para parâmetros indesejados, gerando um código frágil e de difícil leitura.

## 🏗️ Padrões e Conceitos Aplicados

* **Builder Pattern:** A responsabilidade de coletar, montar e validar os dados foi delegada para uma classe especialista (`NotaFiscalBuilder`).
* **Fluent Interface (Interface Fluente):** Os métodos do Builder retornam a própria instância (`return this;`), permitindo o encadeamento de chamadas (ex: `.comCnpj(...).comValorTotal(...)`). Isso torna a leitura do código fluida e semântica.
* **Imutabilidade e Fail-Fast:** A regra de negócio exige que uma Nota Fiscal não seja alterada após emitida. Por isso, a entidade principal não possui métodos *setters*. Além disso, o método final `.build()` valida a integridade dos dados (como a presença do CNPJ e valor maior que zero) antes mesmo do objeto ser criado, falhando rapidamente caso o estado seja inválido.

## 📐 Decisão Arquitetural: O Paradoxo da Visibilidade em Java
Durante a execução da atividade, deparamo-nos com um conflito técnico comum na implementação do Builder em arquivos separados:
1. A instrução exigia que o construtor de `NotaFiscal` fosse estritamente `private`.
2. A instrução exigia que `NotaFiscalBuilder` fosse uma classe separada.

**O Problema:** No Java, se um construtor é `private`, absolutamente **nenhuma** classe externa consegue invocá-lo, nem mesmo o Builder. A solução padrão de mercado para manter o `private` é transformar o Builder em uma *Static Inner Class* (Classe Interna Estática) dentro da própria entidade.

**A Solução Adotada (Package-Private):**
Para respeitar a separação física dos arquivos (`NotaFiscal.java` e `NotaFiscalBuilder.java`) exigida pela atividade, optamos por utilizar a visibilidade de pacote (Package-Private) no construtor da entidade.

Ao remover o modificador `private` (deixando o construtor sem modificador explícito), permitimos que o `NotaFiscalBuilder` — que está na mesma pasta/pacote — acesse o construtor livremente. Ao mesmo tempo, garantimos que classes clientes externas (como a `NotaFiscalApp`) continuem bloqueadas de usar o operador `new NotaFiscal(...)` diretamente, cumprindo integralmente o objetivo da restrição arquitetural.

## ⚙️ Como Executar
Compile as classes e execute a aplicação principal:
```bash
javac *.java
java NotaFiscalApp