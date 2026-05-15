package br.edu.puc.biblioteca.excecao;

public class UsuarioNaoEncontradoException extends BibliotecaException {
    public UsuarioNaoEncontradoException(String matricula) {
        super("Usuario nao encontrado: matricula " + matricula);
    }
}
