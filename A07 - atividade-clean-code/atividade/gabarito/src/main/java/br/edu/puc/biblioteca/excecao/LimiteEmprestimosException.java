package br.edu.puc.biblioteca.excecao;

public class LimiteEmprestimosException extends BibliotecaException {
    public LimiteEmprestimosException(String matricula) {
        super("Usuario atingiu o limite de emprestimos: matricula " + matricula);
    }
}
