package compliance;

public class GerenciadorDeCompliance {

    /* * O uso da palavra-chave 'volatile' é crucial aqui. 
     * Ela garante que múltiplas threads manipulem a variável 'instancia' 
     * lendo e escrevendo diretamente na memória principal, evitando problemas de cache.
     */
    private static volatile GerenciadorDeCompliance instancia;

    /*
     * Construtor privado: Bloqueia a criação de instâncias via operador 'new'
     * em qualquer outra parte do sistema.
     */
    private GerenciadorDeCompliance() {
        System.out.println("[COAF] Estabelecendo a ÚNICA conexão de socket com o Banco Central...");
    }

    /*
     * Método de acesso global com Double-Checked Locking.
     * Justificativa: Sincronizar (synchronized) o método inteiro seria muito lento,
     * pois todas as transações entrariam em uma fila de espera. Com o Double-Checked,
     * nós só bloqueamos as threads na PRIMEIRA vez que a instância for criada.
     */
    public static GerenciadorDeCompliance getInstancia() {
        // 1ª Verificação: Se já existe, retorna rápido sem bloquear o sistema.
        if (instancia == null) {
            
            // Bloqueio (Lock): Apenas uma thread pode entrar neste bloco por vez.
            synchronized (GerenciadorDeCompliance.class) {
                
                // 2ª Verificação: Garante que outra thread não criou a instância 
                // enquanto esta thread estava esperando na "porta" do synchronized.
                if (instancia == null) {
                    instancia = new GerenciadorDeCompliance();
                }
            }
        }
        return instancia;
    }

    // Método de negócio que simula o registro da transação
    public void registrarTransacao(String idTransacao, double valor) {
        System.out.println("[COAF] Registrando transação " + idTransacao + " de R$" + valor + " | Processado por: " + this.hashCode());
    }
}