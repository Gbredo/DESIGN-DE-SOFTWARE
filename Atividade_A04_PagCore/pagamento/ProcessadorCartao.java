package pagamento;

public class ProcessadorCartao implements ProcessadorPagamento {
    @Override
    public void processar(double valor) {
        System.out.println("[CARTÃO] Validando limite com a adquirente... Pagamento de R$" + valor + " aprovado.");
    }
}