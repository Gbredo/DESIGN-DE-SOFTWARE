public class NotaFiscalBuilder {

  int numero;
  String cnpj;
  String razaoSocial;
  double valorTotal = 0.0;
  String observacoes;
  String transportadora;
  String enderecoEntregaAlternativo;
  double descontoCondicional = 0.0;

  // Métodos semânticos encadeáveis [cite: 26]
  public NotaFiscalBuilder comNumero(int numero) {
    this.numero = numero;
    return this;
  }

  public NotaFiscalBuilder comCnpj(String cnpj) {
    this.cnpj = cnpj;
    return this;
  }

  public NotaFiscalBuilder comRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
    return this;
  }

  public NotaFiscalBuilder comValorTotal(double valorTotal) {
    this.valorTotal = valorTotal;
    return this;
  }

  public NotaFiscalBuilder comObservacoes(String observacoes) {
    this.observacoes = observacoes;
    return this;
  }

  public NotaFiscalBuilder comTransportadora(String transportadora) {
    this.transportadora = transportadora;
    return this;
  }

  public NotaFiscalBuilder comEnderecoEntregaAlternativo(String endereco) {
    this.enderecoEntregaAlternativo = endereco;
    return this;
  }

  public NotaFiscalBuilder comDescontoCondicional(double desconto) {
    this.descontoCondicional = desconto;
    return this;
  }

  // Etapa 3: Validação e Construção [cite: 27]
  public NotaFiscal build() {

    // Regra de Negócio: Validação de CNPJ [cite: 29]
    if (this.cnpj == null || this.cnpj.trim().isEmpty()) {
      throw new IllegalStateException("O CNPJ é obrigatório para a emissão da Nota Fiscal.");
    }

    // Regra de Negócio: Validação de Valor Total [cite: 29]
    if (this.valorTotal <= 0.0) {
      throw new IllegalStateException("O valor total dos itens deve ser maior que zero.");
    }

    // Retorna o objeto pronto usando o construtor package-private [cite: 28]
    return new NotaFiscal(this);
  }
}