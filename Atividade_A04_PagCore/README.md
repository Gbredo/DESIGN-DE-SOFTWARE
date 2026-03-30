# 🚀 Atividade A04 - PagCore: Padrões Criacionais

## 🧠 Sobre o Processo de Desenvolvimento e Metodologia
Este projeto foi desenvolvido utilizando uma abordagem de **Pair Programming (Programação em Par) assistida por IA** (Gemini). 

O objetivo principal desta entrega não foi apenas apresentar um código funcional, mas sim compreender profundamente a aplicação prática dos padrões **Singleton** e **Factory Method** em um cenário corporativo (Gateway de Pagamentos). A inteligência artificial foi utilizada ativamente como uma ferramenta de tutoria técnica para auxiliar na transição dos conceitos teóricos (como *Thread-safety* e o Princípio do Aberto/Fechado - OCP) para a implementação real em Java.

Os comentários extensos e técnicos presentes no código-fonte (como as justificativas para o uso do *Double-Checked Locking*) foram mantidos intencionalmente. Eles servem não apenas como justificativa arquitetural para a atividade, mas como uma base de conhecimento pessoal para consultas futuras.

## 🏗️ Padrões Aplicados
* **Singleton (`GerenciadorDeCompliance`):** Implementado com bloqueio de concorrência para garantir acesso único ao recurso do Banco Central.
* **Factory Method (`PagamentoFactory`):** Centralização da lógica de instanciação, blindando o motor principal do Gateway de alterações futuras.