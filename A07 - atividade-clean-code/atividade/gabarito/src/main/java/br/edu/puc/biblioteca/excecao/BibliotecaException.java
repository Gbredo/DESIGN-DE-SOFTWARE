package br.edu.puc.biblioteca.excecao;

public class BibliotecaException extends RuntimeException {
    public BibliotecaException(String mensagem) {
        super(mensagem);
    }
}
