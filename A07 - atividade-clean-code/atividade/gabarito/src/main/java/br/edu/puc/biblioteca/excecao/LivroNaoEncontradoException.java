package br.edu.puc.biblioteca.excecao;

public class LivroNaoEncontradoException extends BibliotecaException {
    public LivroNaoEncontradoException(String isbn) {
        super("Livro nao encontrado: ISBN " + isbn);
    }
}
