public class NotaFiscal {

  // Atributos Obrigatórios
  private final int numero;
  private final String cnpj;
  private final String razaoSocial;
  private final double valorTotal;

  // Atributos Opcionais
  private final String observacoes;
  private final String transportadora;
  private final String enderecoEntregaAlternativo;
  private final double descontoCondicional;

  // Construtor PRIVADO: Impede o uso de "new NotaFiscal(...)" fora do Builder
  // Ele recebe o próprio Builder como parâmetro para extrair os dados já
  // validados.
  NotaFiscal(NotaFiscalBuilder builder) {
    this.numero = builder.numero;
    this.cnpj = builder.cnpj;
    this.razaoSocial = builder.razaoSocial;
    this.valorTotal = builder.valorTotal;
    this.observacoes = builder.observacoes;
    this.transportadora = builder.transportadora;
    this.enderecoEntregaAlternativo = builder.enderecoEntregaAlternativo;
    this.descontoCondicional = builder.descontoCondicional;
  }

  // Apenas métodos Getters (NENHUM Setter)
  public int getNumero() {
    return numero;
  }

  public String getCnpj() {
    return cnpj;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public double getValorTotal() {
    return valorTotal;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public String getTransportadora() {
    return transportadora;
  }

  public String getEnderecoEntregaAlternativo() {
    return enderecoEntregaAlternativo;
  }

  public double getDescontoCondicional() {
    return descontoCondicional;
  }

  // Método estático opcional para facilitar a chamada do Builder
  public static NotaFiscalBuilder builder() {
    return new NotaFiscalBuilder();
  }
}