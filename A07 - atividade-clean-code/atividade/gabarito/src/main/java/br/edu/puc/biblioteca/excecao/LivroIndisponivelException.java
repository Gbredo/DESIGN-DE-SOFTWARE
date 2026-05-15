package br.edu.puc.biblioteca.excecao;

public class LivroIndisponivelException extends BibliotecaException {
    public LivroIndisponivelException(String isbn) {
        super("Livro indisponivel para emprestimo: ISBN " + isbn);
    }
}
