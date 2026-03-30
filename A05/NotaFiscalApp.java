public class NotaFiscalApp {
  public static void main(String[] args) {

    try {
      System.out.println("Tentando instanciar a Nota Fiscal de forma fluente...");

      // Instanciando o Builder
      NotaFiscalBuilder builder = new NotaFiscalBuilder();

      // Construindo a nota passo a passo
      NotaFiscal nf = builder
          .comNumero(1029)
          .comCnpj("12.345.678/0001-99")
          .comRazaoSocial("Empresa X")
          .comValorTotal(5000.00)
          .comObservacoes("Entregar no período da manhã")
          .build();

      System.out
          .println("Sucesso! Nota Fiscal número " + nf.getNumero() + " gerada no valor de R$ " + nf.getValorTotal());

    } catch (IllegalStateException e) {
      System.err.println("Erro de Validação: " + e.getMessage());
    }
  }
}