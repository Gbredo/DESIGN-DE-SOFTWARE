public class PagamentoBoleto implements EstrategiaPagamento {
    @Override
    public double calcularValorFinal(double valorBase) {
        return valorBase + 5.00; // Taxa fixa de R$5,00 para pagamentos via boleto
    }
}