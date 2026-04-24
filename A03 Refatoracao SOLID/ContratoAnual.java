public class ContratoAnual extends Contrato implements Renovavel {

  public ContratoAnual(String titular, double valorBase) {
    super(titular, valorBase);
  }

  @Override
  public void renovar() {
    System.out.println("Renovando o contrato por mais 12 meses.");
    this.valorBase += (this.valorBase * 0.10);
  }
}