package pagamento;

public class PagamentoFactory {
    
    /*
     * Factory Method: Encapsula a lógica de criação.
     * O sistema principal do PagCore apenas chama este método, 
     * ficando totalmente protegido das mudanças técnicas de cada meio de pagamento.
     */
    public static ProcessadorPagamento criarProcessador(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de pagamento não pode ser nulo.");
        }
        
        // Aqui a fábrica toma a decisão de qual classe instanciar
        switch (tipo.toUpperCase()) {
            case "PIX":
                return new ProcessadorPix();
            case "CARTAO":
                return new ProcessadorCartao();
            case "SWIFT":
                return new ProcessadorSwift();
            default:
                throw new IllegalArgumentException("Meio de pagamento desconhecido ou não suportado: " + tipo);
        }
    }
}