public class GerenciadorDeContratos {

  private ServicoPagamento servicoPagamento;
  private ServicoNotificacao servicoNotificacao;
  private ContratoRepository contratoRepository;

  public GerenciadorDeContratos(ServicoPagamento servicoPagamento,
      ServicoNotificacao servicoNotificacao,
      ContratoRepository contratoRepository) {
    this.servicoPagamento = servicoPagamento;
    this.servicoNotificacao = servicoNotificacao;
    this.contratoRepository = contratoRepository;
  }

  public void processarEfetivacao(Contrato contrato, EstrategiaPagamento estrategia, String emailCliente) {

    // 1. Calcula o valor
    double valorFinal = estrategia.calcularValorFinal(contrato.getValorBase());

    // 2. Processa o pagamento
    servicoPagamento.processarPagamento(valorFinal);

    // 3. Salva no banco (o try/catch e o SQL ficam escondidos dentro da implementação do repository)
    contratoRepository.salvar(contrato, valorFinal);

    // 4. Notifica o cliente
    servicoNotificacao.notificarCliente(emailCliente, "Seu contrato foi efetivado com sucesso. Valor: " + valorFinal);
  }
}