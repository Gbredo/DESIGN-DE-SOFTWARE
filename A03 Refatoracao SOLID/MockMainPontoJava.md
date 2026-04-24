Para testar os arquivos java derivados do código legado dos 4 artefatos, deixo aqui um Main.java com *Mocks* (imitações, classes falsas que servem para mostrar uma mensagem na tela,provando que a arquitetura funciona).
Também seria necessário criar um `NotificarEmail()` e `ContratoBancoDeDados()`.

```java
public class Main {

    public static void main(String[] args) {
        
        System.out.println("--- INICIANDO O SISTEMA ---");

        // 1. PREPARAÇÃO DAS DEPENDÊNCIAS (As ferramentas)
        // Aqui nós criamos as instâncias concretas das nossas interfaces.
        // Se no futuro você quiser usar SMS em vez de Email, é só trocar a classe aqui!
        ServicoNotificacao notificacaoEmail = new NotificacaoEmail(); // Classe que você criaria implementando a interface
        ServicoPagamento pagamentoCartao = new ServicoPagamentoCartao();
        ContratoRepository repositorioBanco = new ContratoBancoDeDados(); // Classe que implementa o salvamento no DB

        // 2. INJEÇÃO DE DEPENDÊNCIA (Montando o operário)
        // Entregamos as ferramentas prontas para o Gerenciador. Ele não sabe COMO elas funcionam, só sabe USÁ-LAS.
        GerenciadorDeContratos gerenciador = new GerenciadorDeContratos(pagamentoCartao, notificacaoEmail, repositorioBanco);

        // 3. CRIANDO OS DADOS DO CLIENTE
        // Vamos criar um contrato anual de 1000 reais para o João
        Contrato contratoJoao = new ContratoAnual("João da Silva", 1000.00);

        // 4. ESCOLHENDO A ESTRATÉGIA DE PAGAMENTO (O padrão Strategy que criamos)
        // O João decidiu pagar à vista, o que lhe dá direito a desconto.
        EstrategiaPagamento pagamentoAVista = new PagamentoAVista();

        // 5. EXECUTANDO A REGRA DE NEGÓCIO PRINCIPAL
        System.out.println("\n--- PROCESSANDO EFETIVAÇÃO DO CONTRATO ---");
        
        // Passamos o contrato, a forma que ele escolheu pagar, e o e-mail de contato
        gerenciador.processarEfetivacao(contratoJoao, pagamentoAVista, "joao@email.com");

        System.out.println("\n--- SISTEMA FINALIZADO ---");
    }
}
```

```java
public class NotificacaoEmail implements ServicoNotificacao {

    @Override
    public void notifyClient(String email, String mensagem) {
        // Simulando o envio de um e-mail
        System.out.println("Enviando e-mail para: " + email);
        System.out.println("Conteúdo: " + mensagem);
    }
}
```

```java
public class ContratoBancoDeDados implements ContratoRepository {

    @Override
    public void salvar(Contrato contrato, double valorFinal) {
        // Simulando um insert no banco de dados (escondendo aquele try/catch feio com SQL)
        System.out.println("Salvando no Banco de Dados: Contrato de " + contrato.getTitular() + " no valor de $" + valorFinal);
    }
}
```