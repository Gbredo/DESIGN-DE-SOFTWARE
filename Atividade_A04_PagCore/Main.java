import compliance.GerenciadorDeCompliance;
import pagamento.PagamentoFactory;
import pagamento.ProcessadorPagamento;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Iniciando Gateway de Pagamentos PagCore ---\n");

        // 1. Invocando o Singleton pela primeira vez
        GerenciadorDeCompliance coaf = GerenciadorDeCompliance.getInstancia();

        System.out.println("\n--- Recebendo Transacao 1 ---");
        ProcessadorPagamento pag1 = PagamentoFactory.criarProcessador("PIX");
        pag1.processar(150.00);
        coaf.registrarTransacao("TXN-001", 150.00);

        System.out.println("\n--- Recebendo Transacao 2 ---");
        ProcessadorPagamento pag2 = PagamentoFactory.criarProcessador("CARTAO");
        pag2.processar(3200.50);
        coaf.registrarTransacao("TXN-002", 3200.50);

        System.out.println("\n--- Recebendo Transacao 3 ---");
        ProcessadorPagamento pag3 = PagamentoFactory.criarProcessador("SWIFT");
        pag3.processar(15000.00);
        
        // Tentando pegar a instância DE NOVO
        GerenciadorDeCompliance coafDeNovo = GerenciadorDeCompliance.getInstancia();
        coafDeNovo.registrarTransacao("TXN-003", 15000.00);
        
        System.out.println("\n--- Auditoria de Instancias ---");
        System.out.println("As duas variaveis apontam para o mesmo lugar na memoria?");
        System.out.println(coaf == coafDeNovo ? "SIM! O Singleton funcionou!" : "NAO! Deu erro!");
    }
}