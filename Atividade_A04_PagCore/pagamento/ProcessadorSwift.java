package pagamento;

public class ProcessadorSwift implements ProcessadorPagamento {
    @Override
    public void processar(double valor) {
        System.out.println("[SWIFT] Iniciando remessa internacional... Pagamento de R$" + valor + " em processamento.");
    }
}