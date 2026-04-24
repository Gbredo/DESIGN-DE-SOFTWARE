public class PagamentoAVista implements EstrategiaPagamento {
    @Override
    public double calcularValorFinal(double valorBase) {
        return valorBase * 0.90; // Desconto de 10% para pagamentos à vista
    }
}