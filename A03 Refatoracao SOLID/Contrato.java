public abstract class Contrato {
  protected String titular;
  protected double valorBase;

  public Contrato(String titular, double valorBase) {
    this.titular = titular;
    this.valorBase = valorBase;
  }

  public void assinar() {
    System.out.println("Contrato assinado por: " + titular);
  }

  public double getValorBase() {
    return valorBase;
  }

  public String getTitular() {
    return titular;
  }
}