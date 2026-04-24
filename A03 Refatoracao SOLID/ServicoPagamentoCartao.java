public class ServicoPagamentoCartao implements ServicoPagamento {

  @Override
  public void processarPagamento(double valor) {
    System.out.println("Processando pagamento via API de Cartão de Crédito: $" + valor);
  }
}