package br.edu.puc.biblioteca.dominio;

/**
 * Representa um usuário da biblioteca universitária.
 *
 * Diferenças em relação ao código original (Usr):
 *   - Nome de classe descritivo e convencional.
 *   - Todos os campos são privados (encapsulamento).
 *   - Nomes de campos e métodos revelam intenção.
 *   - TipoUsuario substitui o inteiro 1/2.
 *   - A lógica de verificação de limite vive aqui, não espalhada em Bib.proc().
 */
public class Usuario {

    private final String matricula;
    private final String nome;
    private final String telefone;
    private final TipoUsuario tipo;
    private boolean ativo;
    private int quantidadeEmprestimos;

    public Usuario(String matricula, String nome, String telefone, TipoUsuario tipo) {
        this.matricula             = matricula;
        this.nome                  = nome;
        this.telefone              = telefone;
        this.tipo                  = tipo;
        this.ativo                 = true;
        this.quantidadeEmprestimos = 0;
    }

    /** Retorna true se o usuário está ativo e ainda não atingiu seu limite de empréstimos. */
    public boolean podeEmprestar() {
        return ativo && quantidadeEmprestimos < tipo.getLimiteEmprestimos();
    }

    public void registrarEmprestimo() {
        quantidadeEmprestimos++;
    }

    public void registrarDevolucao() {
        quantidadeEmprestimos--;
    }

    public void desativar() {
        this.ativo = false;
    }

    // --- getters ---

    public String getMatricula()            { return matricula; }
    public String getNome()                 { return nome; }
    public String getTelefone()             { return telefone; }
    public TipoUsuario getTipo()            { return tipo; }
    public boolean isAtivo()               { return ativo; }
    public int getQuantidadeEmprestimos()  { return quantidadeEmprestimos; }
}
