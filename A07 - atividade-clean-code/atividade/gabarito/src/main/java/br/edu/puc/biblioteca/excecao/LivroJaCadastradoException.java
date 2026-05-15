package br.edu.puc.biblioteca.excecao;

public class LivroJaCadastradoException extends BibliotecaException {
    public LivroJaCadastradoException(String isbn) {
        super("Livro ja cadastrado: ISBN " + isbn);
    }
}
