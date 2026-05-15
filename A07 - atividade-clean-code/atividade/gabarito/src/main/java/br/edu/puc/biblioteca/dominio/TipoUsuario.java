package br.edu.puc.biblioteca.dominio;

/**
 * Representa os tipos de usuário da biblioteca e seus respectivos limites de empréstimo.
 *
 * Substitui o uso de inteiros mágicos (1 = aluno, 2 = professor) presentes
 * no código original. O limite de empréstimos deixa de ser verificado por
 * condicionais espalhados pelo código e passa a ser uma propriedade do tipo.
 */
public enum TipoUsuario {

    ALUNO(3),
    PROFESSOR(5);

    private final int limiteEmprestimos;

    TipoUsuario(int limiteEmprestimos) {
        this.limiteEmprestimos = limiteEmprestimos;
    }

    public int getLimiteEmprestimos() {
        return limiteEmprestimos;
    }
}
