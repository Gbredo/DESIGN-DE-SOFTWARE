package pagamento;

public class ProcessadorPix implements ProcessadorPagamento {
    @Override
    public void processar(double valor) {
        System.out.println("[PIX] Conectando ao Banco Central... Pagamento instantâneo de R$" + valor + " aprovado.");
    }
}